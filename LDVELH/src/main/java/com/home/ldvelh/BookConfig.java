package com.home.ldvelh;

import com.home.ldvelh.commons.Utils;
import com.home.ldvelh.model.character.CCCharacter;
import com.home.ldvelh.model.character.Character;
import com.home.ldvelh.model.character.DF02Character;
import com.home.ldvelh.model.character.DF03Character;
import com.home.ldvelh.model.character.DF04Character;
import com.home.ldvelh.model.character.DF08Character;
import com.home.ldvelh.model.character.DF10Character;
import com.home.ldvelh.model.character.DFCharacter;
import com.home.ldvelh.model.character.SOCharacter;
import com.home.ldvelh.ui.activity.AdventureActivity;
import com.home.ldvelh.ui.activity.CCAdventureActivity;
import com.home.ldvelh.ui.activity.DF04AdventureActivity;
import com.home.ldvelh.ui.activity.DFAdventureActivity;
import com.home.ldvelh.ui.dialog.CCTutelaryGodPicker;
import com.home.ldvelh.ui.dialog.DF02SpellPicker;
import com.home.ldvelh.ui.dialog.DF03MagicItemPicker;
import com.home.ldvelh.ui.dialog.PotionPicker;
import com.home.ldvelh.ui.dialog.SOCharacterTypePicker;
import com.home.ldvelh.ui.inflater.FreeAreaInflaterDF02;
import com.home.ldvelh.ui.inflater.FreeAreaInflaterDF10;
import com.home.ldvelh.ui.inflater.FreeAreaInflaterSO;
import com.home.ldvelh.ui.page.CCCombatPage;
import com.home.ldvelh.ui.page.CCEquipmentPage;
import com.home.ldvelh.ui.page.CCGodsPage;
import com.home.ldvelh.ui.page.DF02SpellsPage;
import com.home.ldvelh.ui.page.DF03MagicItemsPage;
import com.home.ldvelh.ui.page.DF04CombatPage;
import com.home.ldvelh.ui.page.DF04ItemsPage;
import com.home.ldvelh.ui.page.DF08MagicStonesPage;
import com.home.ldvelh.ui.page.DFCombatPage;
import com.home.ldvelh.ui.page.ItemsPage;
import com.home.ldvelh.ui.page.MapViewPage;
import com.home.ldvelh.ui.page.SOSpellsPage;
import com.home.ldvelh.ui.page.UtilitiesPage;
import com.home.ldvelh.ui.widget.PageTag;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.home.ldvelh.commons.Constants.METHOD_ADD_CONSUMABLE_FOOD;
import static com.home.ldvelh.commons.Constants.METHOD_ADD_EQUIPMENT;
import static com.home.ldvelh.commons.Constants.METHOD_ADD_NOTE;
import static com.home.ldvelh.commons.Constants.METHOD_SET_GOLD;

public abstract class BookConfig {

    public enum Series {

        CC(R.string.cc), DF(R.string.df), QG(R.string.qg), SO(R.string.so);

        public enum SeriesComparator implements Comparator<Series> {
            INSTANCE;

            @Override
            public int compare(Series s1, Series s2) {
                return s1.nameResId - s2.nameResId;
            }
        }

        private final int nameResId;

        Series(int nameResId) {
            this.nameResId = nameResId;
            configs.put(this, new ArrayList<BookConfig>());
        }

        public int getNameResId() {
            return nameResId;
        }
    }

    private static final Map<Series, List<BookConfig>> configs = new HashMap<>();

    private final int bookThumbnailResId;

    public abstract AdventureConfig createAdventureConfig();

    public int getBookThumbnailResId() {
        return bookThumbnailResId;
    }

    private BookConfig(int bookThumbnailResId) {
        this.bookThumbnailResId = bookThumbnailResId;
    }

    public static Map<Series, List<BookConfig>> getConfigs() {
        if (configs.isEmpty()) {
            addCCBooks();
            addDFBooks();
            addQGBooks();
            addSOBooks();
        }
        return configs;
    }

    private static void addCCBooks() {
        configs.get(Series.CC).add(new BookConfig(R.drawable.tn_cc01) {
            @Override
            public AdventureConfig createAdventureConfig() {
                AdventureConfig config = new AdventureConfig(R.string.cc_01, R.string.cc_01_title, CCAdventureActivity.class, CCCharacter.class, R.layout.activity_adventure_cc);
                config.addCustomCharacterValues(METHOD_ADD_NOTE, Utils.getString(R.string.cc_aethras_jewel));
                config.addCustomCharacterValues(METHOD_ADD_EQUIPMENT, Utils.getString(R.string.cc_club), 1, 1, 0);
                config.addDialog(CCTutelaryGodPicker.class);
                config.addPage(R.string.tab_title_utilities, UtilitiesPage.class, getCCUtilitiesConfig());
                config.addPage(R.string.tab_title_combat, CCCombatPage.class, getCCCombatTags());
                config.addPage(R.string.tab_title_gods, CCGodsPage.class);
                config.addPage(R.string.tab_title_equipment, CCEquipmentPage.class);
                config.addPage(R.string.tab_title_map, MapViewPage.class);
                return config;
            }
        });
        configs.get(Series.CC).add(new BookConfig(R.drawable.tn_cc02) {
            @Override
            public AdventureConfig createAdventureConfig() {
                return null;
            }
        });
        configs.get(Series.CC).add(new BookConfig(R.drawable.tn_cc03) {
            @Override
            public AdventureConfig createAdventureConfig() {
                return null;
            }
        });
    }

    private static void addDFBooks() {
        configs.get(Series.DF).add(new BookConfig(R.drawable.tn_df01) {
            @Override
            public AdventureConfig createAdventureConfig() {
                AdventureConfig config = commonDFConfig(R.string.df_01, R.string.df_01_title, DFAdventureActivity.class, DFCharacter.class);
                config.addCustomCharacterValues(METHOD_SET_GOLD, 1);
                config.addCustomCharacterValues(METHOD_ADD_CONSUMABLE_FOOD, Utils.getString(R.string.food), 10);
                config.addDialog(PotionPicker.class, 2);
                return config;
            }
        });
        configs.get(Series.DF).add(new BookConfig(R.drawable.tn_df02) {
            @Override
            public AdventureConfig createAdventureConfig() {
                AdventureConfig config = commonDFConfig(R.string.df_02, R.string.df_02_title, DFAdventureActivity.class, DF02Character.class);
                config.addDialog(DF02SpellPicker.class);
                config.insertPage(R.string.tab_title_map, R.string.tab_title_spells, DF02SpellsPage.class);
                config.setFreeAreaInflater(FreeAreaInflaterDF02.INSTANCE);
                return config;
            }
        });
        configs.get(Series.DF).add(new BookConfig(R.drawable.tn_df03) {
            @Override
            public AdventureConfig createAdventureConfig() {
                AdventureConfig config = commonDFConfig(R.string.df_03, R.string.df_03_title, DFAdventureActivity.class, DF03Character.class);
                config.addCustomCharacterValues(METHOD_SET_GOLD, 30);
                config.addCustomCharacterValues(METHOD_ADD_CONSUMABLE_FOOD, Utils.getString(R.string.food), 10);
                config.addDialog(PotionPicker.class, 2);
                config.addDialog(DF03MagicItemPicker.class);
                config.insertPage(R.string.tab_title_map, R.string.tab_title_magic_items, DF03MagicItemsPage.class);
                return config;
            }
        });
        configs.get(Series.DF).add(new BookConfig(R.drawable.tn_df04) {
            @Override
            public AdventureConfig createAdventureConfig() {
                AdventureConfig config = new AdventureConfig(R.string.df_04, R.string.df_04_title, DF04AdventureActivity.class, DF04Character.class, R.layout.activity_adventure_df04);
                config.addPage(R.string.tab_title_utilities, UtilitiesPage.class, getDFUtilitiesConfig());
                config.addPage(R.string.tab_title_combat, DF04CombatPage.class, getDF04CombatTags());
                config.addPage(R.string.tab_title_items, DF04ItemsPage.class);
                config.addPage(R.string.tab_title_map, MapViewPage.class);
                return config;
            }
            private List<PageTag> getDF04CombatTags() {
                List<PageTag> combatButtons = new ArrayList<>();
                combatButtons.add(PageTag.ALLOW_DROP);
                combatButtons.add(PageTag.COMBAT_BUTTON_DF04_PHASER);
                combatButtons.add(PageTag.COMBAT_BUTTON_DF04_LEAVE_PLANET);
                combatButtons.add(PageTag.COMBAT_BUTTON_ASSAULT);
                return combatButtons;
            }
        });
        configs.get(Series.DF).add(new BookConfig(R.drawable.tn_df05) {
            @Override
            public AdventureConfig createAdventureConfig() {
                AdventureConfig config = commonDFConfig(R.string.df_05, R.string.df_05_title, DFAdventureActivity.class, DFCharacter.class);
                config.addCustomCharacterValues(METHOD_SET_GOLD, 30);
                config.addCustomCharacterValues(METHOD_ADD_CONSUMABLE_FOOD, Utils.getString(R.string.food), 10);
                config.addDialog(PotionPicker.class, 2);
                return config;
            }
        });
        configs.get(Series.DF).add(new BookConfig(R.drawable.tn_df06) {
            @Override
            public AdventureConfig createAdventureConfig() {
                AdventureConfig config = commonDFConfig(R.string.df_06, R.string.df_06_title, DFAdventureActivity.class, DFCharacter.class);
                config.addCustomCharacterValues(METHOD_ADD_CONSUMABLE_FOOD, Utils.getString(R.string.food), 10);
                config.addDialog(PotionPicker.class, 1);
                return config;
            }
        });
        configs.get(Series.DF).add(new BookConfig(R.drawable.tn_df07) {
            @Override
            public AdventureConfig createAdventureConfig() {
                AdventureConfig config = commonDFConfig(R.string.df_07, R.string.df_07_title, DFAdventureActivity.class, DFCharacter.class);
                config.addCustomCharacterValues(METHOD_ADD_CONSUMABLE_FOOD, Utils.getString(R.string.food), 10);
                config.addDialog(PotionPicker.class, 1);
                return config;
            }
        });
        configs.get(Series.DF).add(new BookConfig(R.drawable.tn_df08) {
            @Override
            public AdventureConfig createAdventureConfig() {
                AdventureConfig config = commonDFConfig(R.string.df_08, R.string.df_08_title, DFAdventureActivity.class, DF08Character.class);
                config.insertPage(R.string.tab_title_map, R.string.tab_title_magic_stones, DF08MagicStonesPage.class);
                return config;
            }
        });
        configs.get(Series.DF).add(new BookConfig(R.drawable.tn_df09) {
            @Override
            public AdventureConfig createAdventureConfig() {
                AdventureConfig config = commonDFConfig(R.string.df_09, R.string.df_09_title, DFAdventureActivity.class, DFCharacter.class);
                config.addCustomCharacterValues(METHOD_ADD_CONSUMABLE_FOOD, Utils.getString(R.string.food), 10);
                config.addDialog(PotionPicker.class, 1);
                return config;
            }
        });
        configs.get(Series.DF).add(new BookConfig(R.drawable.tn_df10) {
            @Override
            public AdventureConfig createAdventureConfig() {
                AdventureConfig config = commonDFConfig(R.string.df_10, R.string.df_10_title, DFAdventureActivity.class, DF10Character.class);
                config.setFreeAreaInflater(FreeAreaInflaterDF10.INSTANCE);
                return config;
            }
        });
        configs.get(Series.DF).add(new BookConfig(R.drawable.tn_df11) {
            @Override
            public AdventureConfig createAdventureConfig() {
                AdventureConfig config = commonDFConfig(R.string.df_11, R.string.df_11_title, DFAdventureActivity.class, DFCharacter.class);
                config.addCustomCharacterValues(METHOD_ADD_CONSUMABLE_FOOD, Utils.getString(R.string.food), 10);
                config.addDialog(PotionPicker.class, 1);
                return config;
            }
        });
    }

    private static void addQGBooks() {
        configs.get(Series.QG).add(new BookConfig(R.drawable.tn_qg01) {
            @Override
            public AdventureConfig createAdventureConfig() {
                return null;
            }
        });
        configs.get(Series.QG).add(new BookConfig(R.drawable.tn_qg02) {
            @Override
            public AdventureConfig createAdventureConfig() {
                return null;
            }
        });
        configs.get(Series.QG).add(new BookConfig(R.drawable.tn_qg03) {
            @Override
            public AdventureConfig createAdventureConfig() {
                return null;
            }
        });
        configs.get(Series.QG).add(new BookConfig(R.drawable.tn_qg04) {
            @Override
            public AdventureConfig createAdventureConfig() {
                return null;
            }
        });
        configs.get(Series.QG).add(new BookConfig(R.drawable.tn_qg05) {
            @Override
            public AdventureConfig createAdventureConfig() {
                return null;
            }
        });
        configs.get(Series.QG).add(new BookConfig(R.drawable.tn_qg06) {
            @Override
            public AdventureConfig createAdventureConfig() {
                return null;
            }
        });
        configs.get(Series.QG).add(new BookConfig(R.drawable.tn_qg07) {
            @Override
            public AdventureConfig createAdventureConfig() {
                return null;
            }
        });
        configs.get(Series.QG).add(new BookConfig(R.drawable.tn_qg08) {
            @Override
            public AdventureConfig createAdventureConfig() {
                return null;
            }
        });
    }

    private static void addSOBooks() {
        configs.get(Series.SO).add(new BookConfig(R.drawable.tn_so01) {
            @Override
            public AdventureConfig createAdventureConfig() {
                return commonSOConfig(R.string.so_01, R.string.so_01_title, 0);
            }
        });
        configs.get(Series.SO).add(new BookConfig(R.drawable.tn_so02) {
            @Override
            public AdventureConfig createAdventureConfig() {
                return commonSOConfig(R.string.so_02, R.string.so_02_title, R.string.so_01);
            }
        });
        configs.get(Series.SO).add(new BookConfig(R.drawable.tn_so03) {
            @Override
            public AdventureConfig createAdventureConfig() {
                return commonSOConfig(R.string.so_03, R.string.so_03_title, R.string.so_02);
            }
        });
        configs.get(Series.SO).add(new BookConfig(R.drawable.tn_so04) {
            @Override
            public AdventureConfig createAdventureConfig() {
                return commonSOConfig(R.string.so_04, R.string.so_04_title, R.string.so_03);
            }
        });
    }

    private static <T extends AdventureActivity, U extends Character> AdventureConfig commonDFConfig(int bookResId, int titleResId, Class<T> adventureClass, Class<U> characterClass) {
        AdventureConfig config = new AdventureConfig(bookResId, titleResId, adventureClass, characterClass, R.layout.activity_adventure);
        config.addPage(R.string.tab_title_utilities, UtilitiesPage.class, getDFUtilitiesConfig());
        config.addPage(R.string.tab_title_combat, DFCombatPage.class, getDFCombatButtons());
        config.addPage(R.string.tab_title_items, ItemsPage.class);
        config.addPage(R.string.tab_title_map, MapViewPage.class);
        return config;
    }

    private static AdventureConfig commonSOConfig(int bookResId, int titleResId, int previousBookResId) {
        AdventureConfig config = new AdventureConfig(bookResId, titleResId, DFAdventureActivity.class, SOCharacter.class, R.layout.activity_adventure, previousBookResId);
        config.addCustomCharacterValues(METHOD_SET_GOLD, 20);
        config.addDialog(SOCharacterTypePicker.class, config);
        config.addPage(R.string.tab_title_utilities, UtilitiesPage.class, getSOUtilitiesConfig());
        config.addPage(R.string.tab_title_combat, DFCombatPage.class, getDFCombatButtons());
        config.addPage(R.string.tab_title_items, ItemsPage.class);
        config.addPage(R.string.tab_title_magic, SOSpellsPage.class);
        config.addPage(R.string.tab_title_map, MapViewPage.class);
        config.setFreeAreaInflater(FreeAreaInflaterSO.INSTANCE);
        return config;
    }

    private static List<PageTag> getCCUtilitiesConfig() {
        List<PageTag> config = new ArrayList<>();
        config.add(PageTag.UTILITY_LAST_PARAGRAPH);
        config.add(PageTag.UTILITY_DICE);
        config.add(PageTag.UTILITY_ZEUS);
        return config;
    }

    private static List<PageTag> getDFUtilitiesConfig() {
        List<PageTag> config = new ArrayList<>();
        config.add(PageTag.UTILITY_LAST_PARAGRAPH);
        config.add(PageTag.UTILITY_DICE);
        config.add(PageTag.UTILITY_LUCK_CHECK);
        return config;
    }

    private static List<PageTag> getSOUtilitiesConfig() {
        List<PageTag> config = new ArrayList<>();
        config.add(PageTag.UTILITY_LAST_PARAGRAPH);
        config.add(PageTag.UTILITY_DICE);
        config.add(PageTag.UTILITY_LUCK_CHECK);
        config.add(PageTag.UTILITY_LIBRA);
        return config;
    }

    private static List<PageTag> getCCCombatTags() {
        List<PageTag> combatButtons = new ArrayList<>();
        combatButtons.add(PageTag.COMBAT_BUTTON_CC_HELP);
        combatButtons.add(PageTag.COMBAT_BUTTON_CC_KILL);
        combatButtons.add(PageTag.COMBAT_BUTTON_CC_SURRENDER);
        combatButtons.add(PageTag.COMBAT_BUTTON_ESCAPE);
        combatButtons.add(PageTag.COMBAT_BUTTON_ASSAULT);
        return combatButtons;
    }

    private static List<PageTag> getDFCombatButtons() {
        List<PageTag> combatButtons = new ArrayList<>();
        combatButtons.add(PageTag.COMBAT_BUTTON_ESCAPE);
        combatButtons.add(PageTag.COMBAT_BUTTON_ASSAULT);
        return combatButtons;
    }
}
