package com.wdiscute.sellingbin;


import net.minecraftforge.common.ForgeConfigSpec;

public class SBConfig
{
    private static final ForgeConfigSpec.Builder BUILDER_CLIENT = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue ALWAYS_SHOW_SELLING_BIN_PRICE = BUILDER_CLIENT
            .comment("Always shows the selling bin price of the item hovered instead of only when holding shift")
            .translation("selling_bin.configuration.always_show_selling_bin_price")
            .define("always_show_selling_bin_price", false);


    static final ForgeConfigSpec SPEC = BUILDER_CLIENT.build();

    private static final ForgeConfigSpec.Builder BUILDER_SERVER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.DoubleValue SELLING_BIN_MULTIPLIER = BUILDER_SERVER
            .comment("Adjusts the selling bin sell rates globally")
            .translation("selling_bin.configuration.selling_in_multiplier")
            .defineInRange("selling_bin_multiplier", 1d, 0d, 9999999d);

    static final ForgeConfigSpec SPEC_SERVER = BUILDER_SERVER.build();
}
