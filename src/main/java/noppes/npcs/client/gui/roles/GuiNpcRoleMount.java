package noppes.npcs.client.gui.roles;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcSlider;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.ISliderListener;
import noppes.npcs.constants.EnumPacketType;
import noppes.npcs.roles.RoleMount;

public class GuiNpcRoleMount extends GuiNPCInterface implements IGuiData, ISliderListener {

    private RoleMount role;
    private boolean dataSaved;

    public GuiNpcRoleMount(EntityNPCInterface npc) {
        super(npc);
        this.role = (RoleMount) npc.roleInterface;
        setBackground("menubg.png");
        xSize = 256;
        ySize = 216;
        closeOnEsc = true;
    }

    @Override
    public void initGui() {
        super.initGui();

        float jumpStrength = roundJumpStrength(role.getJumpStrength());
        role.setJumpStrength(jumpStrength);
        addSlider(new GuiNpcSlider(this, 0, guiLeft + 53, guiTop + 36, null,
                getJumpStrengthText(jumpStrength), jumpStrength / 2.0F));

        addLabel(new GuiNpcLabel(1, "role.mount.allowSprint", guiLeft + 53, guiTop + 76));
        addButton(new GuiNpcButton(1, guiLeft + 153, guiTop + 70, 50, 20,
                new String[] {"gui.no", "gui.yes"}, role.isAllowSprint() ? 1 : 0));

        addButton(new GuiNpcButton(66, guiLeft + 78, guiTop + 186, 100, 20, "gui.done"));
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) {
        if (guibutton.id == 1) {
            role.setAllowSprint(((GuiNpcButton) guibutton).getValue() == 1);
        } else if (guibutton.id == 66) {
            close();
        }
    }

    @Override
    public void onGuiClosed() {
        save();
        super.onGuiClosed();
    }

    @Override
    public void save() {
        if (dataSaved) {
            return;
        }
        dataSaved = true;
        NoppesUtil.sendData(EnumPacketType.MainmenuAdvancedSave,
                npc.advanced.writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void setGuiData(NBTTagCompound compound) {
        role.readEntityFromNBT(compound);
        initGui();
    }

    @Override
    public void mouseDragged(GuiNpcSlider slider) {
        updateJumpStrength(slider);
    }

    @Override
    public void mousePressed(GuiNpcSlider slider) {
        updateJumpStrength(slider);
    }

    @Override
    public void mouseReleased(GuiNpcSlider slider) {
        updateJumpStrength(slider);
    }

    private void updateJumpStrength(GuiNpcSlider slider) {
        if (slider.id != 0) {
            return;
        }

        int step = Math.round(slider.sliderValue * 20.0F);
        float jumpStrength = step / 10.0F;
        slider.sliderValue = step / 20.0F;
        slider.displayString = getJumpStrengthText(jumpStrength);
        role.setJumpStrength(jumpStrength);
    }

    private float roundJumpStrength(float jumpStrength) {
        return Math.round(jumpStrength * 10.0F) / 10.0F;
    }

    private String getJumpStrengthText(float jumpStrength) {
        return StatCollector.translateToLocal("role.mount.jumpStrength") + ": " + jumpStrength;
    }
}
