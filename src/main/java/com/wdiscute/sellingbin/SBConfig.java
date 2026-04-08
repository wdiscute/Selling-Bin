package com.wdiscute.sellingbin;


import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "selling_bin")
public class SBConfig implements ConfigData
{
    public static SBConfig config;

    @ConfigEntry.Category("client")
    @Comment("Enable the client-side feature")
    public boolean always_show = true;

    @ConfigEntry.Category("server")
    @ConfigEntry.Gui.Excluded  // Hide from client config screens
    @Comment("Server-side multiplier")
    public double multiplier = 1.0;
}


//private static final ModConfigSpec.Builder BUILDER_CLIENT = new ModConfigSpec.Builder();

//public static final boolean ALWAYS_SHOW_SELLING_BIN_PRICE = false; //BUILDER_CLIENT
//            .comment("Always shows the selling bin price of the item hovered instead of only when holding shift")
//            .translation("selling_bin.configuration.always_show_selling_bin_price")
//            .define("always_show_selling_bin_price", false);


//static final ModConfigSpec SPEC = BUILDER_CLIENT.build();

//private static final ModConfigSpec.Builder BUILDER_SERVER = new ModConfigSpec.Builder();

//public static final double SELLING_BIN_MULTIPLIER = 1d; //BUILDER_SERVER
//            .comment("Adjusts the selling bin sell rates globally")
//            .translation("selling_bin.configuration.selling_in_multiplier")
//            .defineInRange("selling_bin_multiplier", 1d, 0d, 9999999d);

//static final ModConfigSpec SPEC_SERVER = BUILDER_SERVER.build();
//}
