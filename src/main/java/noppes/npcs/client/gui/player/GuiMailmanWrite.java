package noppes.npcs.client.gui.player;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.gui.util.GuiButtonNextPage;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiMailmanWrite extends GuiScreen
{
    private static final ResourceLocation bookGuiTextures = new ResourceLocation("textures/gui/book.png");

    /** The player editing the book */
    private final NBTTagCompound itemstackBook;

    /** Update ticks since the gui was opened */
    private int updateCount;
    private int bookImageWidth = 192;
    private int bookImageHeight = 192;
    private int bookTotalPages = 1;
    private int currPage;
    private NBTTagList bookPages;
    private GuiButtonNextPage buttonNextPage;
    private GuiButtonNextPage buttonPreviousPage;
    
    private boolean canEdit;

    /** The GuiButton to sign this book. */
    
    private GuiScreen parent;

    public GuiMailmanWrite(GuiScreen parent, NBTTagCompound compound, boolean canEdit)
    {
    	this.parent = parent;
        this.itemstackBook = compound;
        this.canEdit = canEdit;

        if(itemstackBook.hasKey("pages"))
        	this.bookPages = itemstackBook.getTagList("pages");

        if (this.bookPages != null)
        {
            this.bookPages = (NBTTagList)this.bookPages.copy();
            this.bookTotalPages = this.bookPages.tagCount();

            if (this.bookTotalPages < 1)
            {
                this.bookTotalPages = 1;
            }
        }
        else
        {
            this.bookPages = new NBTTagList("pages");
            this.bookPages.appendTag(new NBTTagString("1", ""));
            this.bookTotalPages = 1;
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
        ++this.updateCount;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.buttonList.clear();
        Keyboard.enableRepeatEvents(true);


        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, 4 + this.bookImageHeight, 98, 20, I18n.getString("gui.done")));


        int i = (this.width - this.bookImageWidth) / 2;
        byte b0 = 2;
        this.buttonList.add(this.buttonNextPage = new GuiButtonNextPage(1, i + 120, b0 + 154, true));
        this.buttonList.add(this.buttonPreviousPage = new GuiButtonNextPage(2, i + 38, b0 + 154, false));
        this.updateButtons();
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    private void updateButtons()
    {
        this.buttonNextPage.drawButton = (this.currPage < this.bookTotalPages - 1 || this.canEdit);
        this.buttonPreviousPage.drawButton = this.currPage > 0;
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.enabled)
        {
            if (par1GuiButton.id == 0)
            {
                close();
            }
            else if (par1GuiButton.id == 1)
            {
                if (this.currPage < this.bookTotalPages - 1)
                {
                    ++this.currPage;
                }
                else if (this.canEdit)
                {
                    this.addNewPage();

                    if (this.currPage < this.bookTotalPages - 1)
                    {
                        ++this.currPage;
                    }
                }
            }
            else if (par1GuiButton.id == 2)
            {
                if (this.currPage > 0)
                {
                    --this.currPage;
                }
            }

            this.updateButtons();
        }
    }

    private void addNewPage()
    {
        if (this.bookPages != null && this.bookPages.tagCount() < 50)
        {
            this.bookPages.appendTag(new NBTTagString("" + (this.bookTotalPages + 1), ""));
            ++this.bookTotalPages;
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2)
    {
        if (par2 == 1){
        	close();
        }
        if(canEdit)
        	this.keyTypedInBook(par1, par2);
        
    }

    /**
     * Processes keystrokes when editing the text of a book
     */
    private void keyTypedInBook(char par1, int par2)
    {
        switch (par1)
        {
            case 22:
                this.func_74160_b(GuiScreen.getClipboardString());
                return;
            default:
                switch (par2)
                {
                    case 14:
                        String s = this.func_74158_i();

                        if (s.length() > 0)
                        {
                            this.func_74159_a(s.substring(0, s.length() - 1));
                        }

                        return;
                    case 28:
                    case 156:
                        this.func_74160_b("\n");
                        return;
                    default:
                        if (ChatAllowedCharacters.isAllowedCharacter(par1))
                        {
                            this.func_74160_b(Character.toString(par1));
                        }
                }
        }
    }

    private String func_74158_i()
    {
        if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount())
        {
            NBTTagString nbttagstring = (NBTTagString)this.bookPages.tagAt(this.currPage);
            return nbttagstring.toString();
        }
        else
        {
            return "";
        }
    }

    private void func_74159_a(String par1Str)
    {
        if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount())
        {
            NBTTagString nbttagstring = (NBTTagString)this.bookPages.tagAt(this.currPage);
            nbttagstring.data = par1Str;
        }
    }

    private void func_74160_b(String par1Str)
    {
        String s1 = this.func_74158_i();
        String s2 = s1 + par1Str;
        int i = this.fontRenderer.splitStringWidth(s2 + "" + EnumChatFormatting.BLACK + "_", 118);

        if (i <= 118 && s2.length() < 256)
        {
            this.func_74159_a(s2);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(bookGuiTextures);
        int k = (this.width - this.bookImageWidth) / 2;
        byte b0 = 2;
        this.drawTexturedModalRect(k, b0, 0, 0, this.bookImageWidth, this.bookImageHeight);
        String s;
        String s1;
        int l;

        s = String.format(I18n.getString("book.pageIndicator"), new Object[] {Integer.valueOf(this.currPage + 1), Integer.valueOf(this.bookTotalPages)});
        s1 = "";

        if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount())
        {
            NBTTagString nbttagstring = (NBTTagString)this.bookPages.tagAt(this.currPage);
            s1 = nbttagstring.toString();
        }
        
        if(canEdit){
	        if (this.fontRenderer.getBidiFlag())
	        {
	            s1 = s1 + "_";
	        }
	        else if (this.updateCount / 6 % 2 == 0)
	        {
	            s1 = s1 + "" + EnumChatFormatting.BLACK + "_";
	        }
	        else
	        {
	            s1 = s1 + "" + EnumChatFormatting.GRAY + "_";
	        }
        }

        l = this.fontRenderer.getStringWidth(s);
        this.fontRenderer.drawString(s, k - l + this.bookImageWidth - 44, b0 + 16, 0);
        this.fontRenderer.drawSplitString(s1, k + 36, b0 + 16 + 16, 116, 0);
        

        super.drawScreen(par1, par2, par3);
    }
    
    public void close(){
    	itemstackBook.setTag("pages", bookPages);
    	mc.displayGuiScreen(parent);
    }

    static ResourceLocation func_110404_g()
    {
        return bookGuiTextures;
    }
}
