package noppes.npcs.client.gui.roles;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.constants.EnumPacketType;
import noppes.npcs.constants.EnumStockReset;
import noppes.npcs.roles.RoleTrader;

public class SubGuiNpcTraderStock extends SubGuiInterface implements ITextfieldListener {

	private RoleTrader role;

	public SubGuiNpcTraderStock(RoleTrader role) {
		this.role = role;
		setBackground("menubg.png");
		xSize = 256;
		ySize = 256;
		closeOnEsc = true;
	}

	@Override
	public void initGui() {
		super.initGui();

		addLabel(new GuiNpcLabel(0, "stock.enable", guiLeft + 5, guiTop + 10));
		addButton(new GuiNpcButton(0, guiLeft + 120, guiTop + 5, 131, 20, new String[]{"gui.no", "gui.yes"}, role.stock.enableStock ? 1 : 0));

		addLabel(new GuiNpcLabel(1, "stock.resettype", guiLeft + 5, guiTop + 34));
		addButton(new GuiNpcButton(1, guiLeft + 80, guiTop + 29, 171, 20, EnumStockReset.getDisplayNames(), role.stock.resetType.ordinal()));

		if(role.stock.resetType == EnumStockReset.MCCUSTOM || role.stock.resetType == EnumStockReset.RLCUSTOM) {
			addLabel(new GuiNpcLabel(2, "stock.customtime", guiLeft + 5, guiTop + 58));
			addTextField(new GuiNpcTextField(10, this, fontRenderer, guiLeft + 120, guiTop + 53, 131, 20, role.stock.customResetTime + ""));
		}

		addLabel(new GuiNpcLabel(3, "stock.maxstock", guiLeft + 5, guiTop + 77));
		for(int i = 0; i < 18; i++) {
			int x = guiLeft + 5 + i % 3 * 83;
			int y = guiTop + 88 + i / 3 * 21;
			addLabel(new GuiNpcLabel(10 + i, (i + 1) + ":", x, y + 6));
			String value = role.stock.maxStock[i] < 0 ? "" : role.stock.maxStock[i] + "";
			addTextField(new GuiNpcTextField(100 + i, this, fontRenderer, x + 18, y, 58, 20, value));
		}

		addButton(new GuiNpcButton(2, guiLeft + 5, guiTop + 232, 120, 20, "stock.resetnow"));
		addButton(new GuiNpcButton(66, guiLeft + 131, guiTop + 232, 120, 20, "gui.done"));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		// 手动绘制输入框白色背景（确保可见）
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		// customTime 输入框背景
		if(role.stock.resetType == EnumStockReset.MCCUSTOM || role.stock.resetType == EnumStockReset.RLCUSTOM) {
			drawRect(guiLeft + 120, guiTop + 53, guiLeft + 251, guiTop + 73, 0xFFFFFFFF);
		}
		// 18 个 maxStock 输入框背景
		for(int i = 0; i < 18; i++) {
			int x = guiLeft + 5 + i % 3 * 83;
			int y = guiTop + 88 + i / 3 * 21;
			drawRect(x + 18, y, x + 76, y + 20, 0xFFFFFFFF);
		}

		// 重新绘制输入框（用 getTextField 访问）
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GuiNpcTextField customTime = getTextField(10);
		if(customTime != null)
			customTime.drawTextBox();
		for(int i = 0; i < 18; i++) {
			GuiNpcTextField tf = getTextField(100 + i);
			if(tf != null)
				tf.drawTextBox();
		}
	}

	@Override
	public void buttonEvent(GuiButton guibutton) {
		if(guibutton.id == 0) {
			role.stock.enableStock = ((GuiNpcButton)guibutton).getValue() == 1;
		}
		else if(guibutton.id == 1) {
			updateStock();
			initGui();
		}
		else if(guibutton.id == 2) {
			updateStock();
			long now = role.stock.resetType.isRealTime() ? System.currentTimeMillis() : role.npc.worldObj.getTotalWorldTime();
			role.stock.resetStock(now);
		}
		else if(guibutton.id == 66) {
			close();
		}
	}

	@Override
	public void unFocused(GuiNpcTextField textfield) {
		if(textfield.id == 10) {
			if(!textfield.isEmpty())
				role.stock.customResetTime = textfield.getInteger();
		}
		else if(textfield.id >= 100 && textfield.id < 118) {
			int slot = textfield.id - 100;
			if(textfield.isEmpty()) {
				role.stock.maxStock[slot] = -1;
			} else {
				role.stock.maxStock[slot] = textfield.getInteger();
				if(role.stock.maxStock[slot] < 0)
					textfield.setText("");
			}
		}
	}

	private void updateStock() {
		GuiNpcTextField.unfocus();
		role.stock.enableStock = getButton(0).getValue() == 1;
		role.stock.resetType = EnumStockReset.values()[getButton(1).getValue()];
		GuiNpcTextField customTime = getTextField(10);
		if(customTime != null) {
			try {
				role.stock.customResetTime = Math.max(0, Long.parseLong(customTime.getText()));
			}
			catch(NumberFormatException e) {
				role.stock.customResetTime = 0;
			}
		}
		for(int i = 0; i < 18; i++) {
			GuiNpcTextField textfield = getTextField(100 + i);
			if(textfield.isEmpty())
				role.stock.maxStock[i] = -1;
			else {
				try {
					role.stock.maxStock[i] = Math.max(-1, Integer.parseInt(textfield.getText()));
				}
				catch(NumberFormatException e) {
					role.stock.maxStock[i] = -1;
				}
			}
		}
		role.stock.validateStock();
	}

	@Override
	public void save() {
		updateStock();
		NoppesUtil.sendData(EnumPacketType.TraderStockSave, role.stock.writeToNBT(new NBTTagCompound()));
	}

	@Override
	public void close() {
		save();
		super.close();
	}
}
