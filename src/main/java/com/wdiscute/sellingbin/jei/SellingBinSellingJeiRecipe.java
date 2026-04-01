package com.wdiscute.sellingbin.jei;

import com.wdiscute.sellingbin.SellingBin;
import com.wdiscute.sellingbin.registry.SBBlocks;
import com.wdiscute.sellingbin.registry.SBDataMaps;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class SellingBinSellingJeiRecipe extends AbstractRecipeCategory<SellingBinSellingJeiRecipe.Recipe>
{

    public SellingBinSellingJeiRecipe(IGuiHelper guiHelper)
    {
        super(
                Recipe.TYPE,
                Component.translatable("emi.category.selling_bin.selling_bin_selling"),
                guiHelper.createDrawableItemLike(SBBlocks.SELLING_BIN),
                SellingBinJeiPlugin.sellables.isEmpty() ? 350 : 120,
                SellingBinJeiPlugin.sellables.isEmpty() ? 100 : 20
        );
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, Recipe recipe, IFocusGroup focuses)
    {
        if (SellingBinJeiPlugin.sellables.isEmpty()) return;

        builder.addInputSlot(5, 2)
                .add(recipe.item().getDefaultInstance());

        builder.addOutputSlot(45, 2)
                .addItemStacks(SellingBinJeiPlugin.currencies.isEmpty() ? List.of(Items.BARRIER.getDefaultInstance()) : SellingBinJeiPlugin.currencies);
    }

    @Override
    public void draw(Recipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY)
    {
        if (SellingBinJeiPlugin.sellables.isEmpty())
        {
            for (int i = 0; i < 10; i++)
                guiGraphics.text(Minecraft.getInstance().font, Component.translatable("gui.selling_bin.selling.empty." + i), 5, 1 + i * 10, 0xff000000, false);
            return;
        }

        Font font = Minecraft.getInstance().font;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SellingBinJeiPlugin.ARROW, 25, 2, 192, 16, 16, 16, 16, 16, 256, 256);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SellingBinJeiPlugin.SLOT_BACKGROUND, 4, 1, 0, 0, 18, 18, 18, 18, 18, 18);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SellingBinJeiPlugin.SLOT_BACKGROUND, 44, 1, 0, 0, 18, 18, 18, 18, 18, 18);

        guiGraphics.text(font, Component.translatable("gui.selling_bin.base_price"), 67, 1, 0xff000000, false);
        guiGraphics.text(font, Component.literal(recipe.price + " "), 67, 11, 0xff000000, false);

        if (mouseX > 110 && mouseX < 120 && mouseY > 11 && mouseY < 21)
            renderTooltip(guiGraphics, recipe.comps, (int) mouseX, (int) mouseY);

        if (!recipe.comps.isEmpty())
            guiGraphics.text(font, Component.literal("[!]"), 111, 13, 0xff880000, false);
    }

    public void renderTooltip(GuiGraphicsExtractor guiGraphics, List<Component> components, int mouseX, int mouseY)
    {
        var clientTooltipComponents = components.stream().map(Component::getVisualOrderText).map(ClientTooltipComponent::create).toList();

        Identifier identifier = ItemStack.EMPTY.get(DataComponents.TOOLTIP_STYLE);

        guiGraphics.tooltip(Minecraft.getInstance().font,
                clientTooltipComponents,
                mouseX,
                mouseY,
                DefaultTooltipPositioner.INSTANCE,
                identifier
        );
    }

    @Override
    public @Nullable Identifier getIdentifier(Recipe recipe)
    {
        return BuiltInRegistries.ITEM.getKey(recipe.item());
    }

    public record Recipe(Item item, SBDataMaps.ItemValue itemValue, int price, List<Component> comps)
    {
        public static final IRecipeType<Recipe> TYPE = IRecipeType.create(SellingBin.rl("selling_bin_sellable"), SellingBinSellingJeiRecipe.Recipe.class);
    }
}
