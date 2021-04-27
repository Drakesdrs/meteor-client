  package minegame159.meteorclient.modules.combat;

import com.google.common.collect.Streams;
import java.util.Comparator;
import java.util.Optional;
import meteordevelopment.orbit.EventHandler;
import minegame159.meteorclient.events.world.TickEvent;
import minegame159.meteorclient.modules.Categories;
import minegame159.meteorclient.modules.Module;
import minegame159.meteorclient.settings.DoubleSetting;
import minegame159.meteorclient.settings.EnumSetting;
import minegame159.meteorclient.settings.Setting;
import minegame159.meteorclient.settings.SettingGroup;
import minegame159.meteorclient.utils.player.PlayerUtils;
import net.minecraft.class_1268;
import net.minecraft.class_1511;
import net.minecraft.class_1792;
import net.minecraft.class_1802;

public class AntiCrystal extends Module {
   private final SettingGroup sgGeneral;
   private final Setting<AntiCrystal.Mode> mode;
   private final Setting<Double> range;
   private int buttonSlot;
   private int pressurePlateSlot;

   public AntiCrystal() {
      super(Categories.Combat, "AntiCrystal", "Stops End Crystals from doing damage to you.");
      this.sgGeneral = this.settings.getDefaultGroup();
      this.mode = this.sgGeneral.add((new EnumSetting.Builder()).name("mode").description("The mode at which AntiCrystal operates.").defaultValue(AntiCrystal.Mode.PressurePlate).build());
      this.range = this.sgGeneral.add((new DoubleSetting.Builder()).name("range").description("The range to place Pressure Plates/Buttons.").min(1.0D).max(10.0D).defaultValue(1.0D).build());
      this.buttonSlot = -1;
      this.pressurePlateSlot = -1;
   }

   @EventHandler
   private void onTick(TickEvent.Post event) {
      assert this.mc.field_1687 != null;

      assert this.mc.field_1724 != null;

      Optional<class_1511> crystalTarget = Streams.stream(this.mc.field_1687.method_18112()).filter((e) -> {
         return e instanceof class_1511;
      }).filter((e) -> {
         return (double)e.method_5739(this.mc.field_1724) <= (Double)this.range.get() * 2.0D;
      }).filter((e) -> {
         return this.mc.field_1687.method_8320(e.method_24515()).method_26215();
      }).min(Comparator.comparingDouble((o) -> {
         return (double)o.method_5739(this.mc.field_1724);
      })).map((e) -> {
         return (class_1511)e;
      });
      crystalTarget.ifPresent((crystal) -> {
         this.findSlots();
         if (this.mode.get() == AntiCrystal.Mode.PressurePlate) {
            if (this.pressurePlateSlot == -1) {
               return;
            }

            PlayerUtils.placeBlock(crystal.method_24515(), this.pressurePlateSlot, class_1268.field_5808);
         } else if (this.mode.get() == AntiCrystal.Mode.Button) {
            if (this.buttonSlot == -1) {
               return;
            }

            PlayerUtils.placeBlock(crystal.method_24515(), this.buttonSlot, class_1268.field_5808);
         }

      });
   }

   private void findSlots() {
      assert this.mc.field_1724 != null;

      this.buttonSlot = -1;
      this.pressurePlateSlot = -1;

      for(int i = 0; i < 9; ++i) {
         class_1792 it = this.mc.field_1724.field_7514.method_5438(i).method_7909();
         if (it != class_1802.field_8605 && it != class_1802.field_8174 && it != class_1802.field_22004 && it != class_1802.field_8531 && it != class_1802.field_8887 && it != class_1802.field_8780 && it != class_1802.field_23834 && it != class_1802.field_8048 && it != class_1802.field_8781 && it != class_1802.field_22005) {
            if (it == class_1802.field_8173 || it == class_1802.field_8779 || it == class_1802.field_21993 || it == class_1802.field_8886 || it == class_1802.field_8047 || it == class_1802.field_8391 || it == class_1802.field_23835 || it == class_1802.field_8707 || it == class_1802.field_8667 || it == class_1802.field_21994) {
               this.pressurePlateSlot = i;
            }
         } else {
            this.buttonSlot = i;
         }
      }

   }

   public static enum Mode {
      PressurePlate,
      Button;
   }
}
    
