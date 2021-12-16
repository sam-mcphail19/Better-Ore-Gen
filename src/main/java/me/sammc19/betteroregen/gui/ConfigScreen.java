package me.sammc19.betteroregen.gui;

import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.SpruceTexts;
import dev.lambdaurora.spruceui.background.DirtTexturedBackground;
import dev.lambdaurora.spruceui.option.SpruceDoubleInputOption;
import dev.lambdaurora.spruceui.option.SpruceIntegerInputOption;
import dev.lambdaurora.spruceui.option.SpruceStringOption;
import dev.lambdaurora.spruceui.screen.SpruceScreen;
import dev.lambdaurora.spruceui.util.RenderUtil;
import dev.lambdaurora.spruceui.widget.SpruceButtonWidget;
import dev.lambdaurora.spruceui.widget.SpruceLabelWidget;
import dev.lambdaurora.spruceui.widget.container.SpruceContainerWidget;
import dev.lambdaurora.spruceui.widget.container.SpruceOptionListWidget;
import dev.lambdaurora.spruceui.widget.container.tabbed.SpruceTabbedWidget;
import me.sammc19.betteroregen.BetterOreGen;
import me.sammc19.betteroregen.config.BetterOreGenConfig;
import me.sammc19.betteroregen.generation.OreVein;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ConfigScreen extends SpruceScreen {

    private final Screen parent;
    private SpruceTabbedWidget tabbedWidget;

    public Consumer<SpruceButtonWidget> resetConsumer;

    public ConfigScreen(@Nullable Screen parent) {
        super(Text.of("Text"));

        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        if (this.client == null)
            return;

        this.resetConsumer = btn -> this.init(this.client, this.client.getWindow().getScaledWidth(), this.client.getWindow().getScaledHeight());

        this.tabbedWidget = new SpruceTabbedWidget(Position.origin(), this.width, this.height, null, Math.max(100, this.width / 8), 0);

        for (int i = 0; i < BetterOreGen.oreVeins.size(); i++) {
            int finalI = i;
            this.tabbedWidget.addTabEntry(Text.of(BetterOreGen.oreVeins.get(finalI).name), null,
                    this.tabContainerBuilder((width, height) -> this.buildVeinOption(Position.origin(), width, height, BetterOreGen.oreVeins.get(finalI))));
        }
        for (int i = 0; i < 15; i++) {
            this.tabbedWidget.addTabEntry(Text.of(BetterOreGen.oreVeins.get(0).name), null,
                    this.tabContainerBuilder((width, height) -> this.buildVeinOption(Position.origin(), width, height, BetterOreGen.oreVeins.get(0))));
        }
        this.addDrawableChild(this.tabbedWidget);
    }

    @Override
    public void removed() {
        super.removed();
        BetterOreGenConfig.save();
    }

    @Override
    public void renderTitle(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 8, 16777215);
    }

    private SpruceTabbedWidget.ContainerFactory tabContainerBuilder(SpruceTabbedWidget.ContainerFactory innerFactory) {
        return (width, height) -> this.buildTabContainer(width, height, innerFactory);
    }

    private SpruceContainerWidget buildTabContainer(int width, int height, SpruceTabbedWidget.ContainerFactory factory) {
        SpruceContainerWidget container = new SpruceContainerWidget(Position.origin(), width, height);
        SpruceLabelWidget label = new SpruceLabelWidget(Position.of(0, 18), this.title.copy().formatted(Formatting.WHITE), width);
        label.setCentered(true);
        container.addChild(label);

        var innerWidget = factory.build(width, height - this.getTextHeight() - 29);
        innerWidget.getPosition().setRelativeY(43);
        container.addChild(innerWidget);

        container.setBackground((matrices, widget, vOffset, mouseX, mouseY, delta) -> {
            if (this.client.world != null) {
                this.fillGradient(matrices, widget.getX(), widget.getY(),
                        widget.getX() + widget.getWidth(), innerWidget.getY(),
                        0xc0101010, 0xd0101010);
                this.fillGradient(matrices, widget.getX(), innerWidget.getY() + innerWidget.getHeight(),
                        widget.getX() + widget.getWidth(), widget.getY() + widget.getHeight(),
                        0xc0101010, 0xd0101010);
            } else {
                DirtTexturedBackground bg = (DirtTexturedBackground) DirtTexturedBackground.NORMAL;
                RenderUtil.renderBackgroundTexture(widget.getX(), widget.getY(),
                        widget.getWidth(), innerWidget.getY() - widget.getY(),
                        vOffset / 32.f, bg.red(), bg.green(), bg.blue(), bg.alpha());
                RenderUtil.renderBackgroundTexture(widget.getX(), innerWidget.getY() + innerWidget.getHeight(),
                        widget.getWidth(), widget.getHeight() - (innerWidget.getY() + innerWidget.getHeight()),
                        vOffset / 32.f, bg.red(), bg.green(), bg.blue(), bg.alpha());
            }
        });

        container.addChild(new SpruceButtonWidget(Position.of(this, width / 2 - 155 + 160, height - 29), 150, 20,
                SpruceTexts.GUI_DONE,
                btn -> this.client.setScreen(this.parent)));

        return container;
    }

    private int getTextHeight() {
        return (5 + this.textRenderer.fontHeight) * 3 + 5;
    }

    public SpruceOptionListWidget buildVeinOption(Position position, int width, int height, OreVein oreVein) {
        var list = new SpruceOptionListWidget(position, width, height);
        list.addSingleOptionEntry(
                new SpruceStringOption("betteroregen.option.vein.blocks",
                        () -> oreVeinBlocksListToJsonString(oreVein.blocks),
                        newValue -> oreVein.blocks = jsonStringToOreVeinBlocksList(newValue),
                        null,
                        Text.of("blocks")
                ));
        list.addSingleOptionEntry(
                new SpruceStringOption("betteroregen.option.vein.blockweights",
                        () -> oreVeinBlockWeightsListToJsonString(oreVein.blockWeights),
                        newValue -> oreVein.blockWeights = jsonStringToOreVeinBlockWeightsList(newValue),
                        null,
                        Text.of("block weights")
                ));
        list.addSingleOptionEntry(
                new SpruceIntegerInputOption("betteroregen.option.vein.ymin",
                        () -> oreVein.yMin,
                        newValue -> oreVein.yMin = newValue,
                        Text.of("ymin")
                ));
        list.addSingleOptionEntry(
                new SpruceIntegerInputOption("betteroregen.option.vein.ymax",
                        () -> oreVein.yMax,
                        newValue -> oreVein.yMax = newValue,
                        Text.of("ymax")
                ));
        list.addSingleOptionEntry(
                new SpruceIntegerInputOption("betteroregen.option.vein.size",
                        () -> oreVein.size,
                        newValue -> oreVein.size = newValue,
                        Text.of("size")
                ));
        list.addSingleOptionEntry(
                new SpruceDoubleInputOption("betteroregen.option.vein.frequency",
                        () -> oreVein.frequency,
                        newValue -> oreVein.frequency = newValue,
                        Text.of("frequency")
                ));
        list.addSingleOptionEntry(
                new SpruceDoubleInputOption("betteroregen.option.vein.density",
                        () -> oreVein.density,
                        newValue -> oreVein.density = newValue,
                        Text.of("density")
                ));

        return list;
    }

    private static String oreVeinBlocksListToJsonString(ArrayList<Block> blocks) {
        StringBuilder toReturn = new StringBuilder();

        for (Block block : blocks) {
            toReturn.append(Registry.BLOCK.getId(block)).append(",");
        }

        if (toReturn.length() > 1) {
            toReturn.deleteCharAt(toReturn.length() - 1);
        }

        return toReturn.toString();
    }

    private static String oreVeinBlockWeightsListToJsonString(ArrayList<Integer> blockWeights) {
        StringBuilder toReturn = new StringBuilder();

        for (int blockWeight : blockWeights) {
            toReturn.append(blockWeight).append(",");
        }

        if (toReturn.length() > 1) {
            toReturn.deleteCharAt(toReturn.length() - 1);
        }

        return toReturn.toString();
    }

    private static ArrayList<Block> jsonStringToOreVeinBlocksList(String listString) {
        return new ArrayList<>();
    }

    private static ArrayList<Integer> jsonStringToOreVeinBlockWeightsList(String listString) {
        return new ArrayList<>();
    }
}
