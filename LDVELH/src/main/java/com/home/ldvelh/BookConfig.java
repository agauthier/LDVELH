package com.home.ldvelh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.home.ldvelh.commons.Utils;
import com.home.ldvelh.model.character.CCCharacter;
import com.home.ldvelh.model.character.DF02Character;
import com.home.ldvelh.model.character.DF03Character;
import com.home.ldvelh.model.character.DF04Character;
import com.home.ldvelh.model.character.DF08Character;
import com.home.ldvelh.model.character.DF10Character;
import com.home.ldvelh.model.character.DFCharacter;
import com.home.ldvelh.model.character.SOCharacter;
import com.home.ldvelh.ui.activity.CCAdventureActivity;
import com.home.ldvelh.ui.activity.DF04AdventureActivity;
import com.home.ldvelh.ui.activity.DFAdventureActivity;
import com.home.ldvelh.ui.dialog.CCTutelaryGodPicker;
import com.home.ldvelh.ui.dialog.DF02SpellPicker;
import com.home.ldvelh.ui.dialog.DF03MagicItemPicker;
import com.home.ldvelh.ui.dialog.PotionPicker;
import com.home.ldvelh.ui.inflater.FreeAreaInflaterDF02;
import com.home.ldvelh.ui.inflater.FreeAreaInflaterDF10;
import com.home.ldvelh.ui.inflater.FreeAreaInflaterSO;
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
import com.home.ldvelh.ui.widget.PageInfo.Tag;

public abstract class BookConfig {

	public enum Series {

		CC(R.string.cc), DF(R.string.df), QG(R.string.qg), SO(R.string.so);

		public enum SeriesComparator implements Comparator<Series> {
			INSTANCE;

			@Override public int compare(Series s1, Series s2) {
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
			@Override public AdventureConfig createAdventureConfig() {
				AdventureConfig config = new AdventureConfig(R.string.cc_01, R.string.cc_01_title, CCAdventureActivity.class, CCCharacter.class, R.layout.activity_adventure_cc);
				config.addCustomCharacterValues(Utils.getString(R.string.add_note), Utils.getString(R.string.cc_aethras_jewel));
				config.addDialog(CCTutelaryGodPicker.class);
				config.addPage(R.string.tab_title_utilities, UtilitiesPage.class, getCCUtilitiesConfig());
				// config.addPage(R.string.tab_title_combat,
				// CombatPageFragment.class);
				config.addPage(R.string.tab_title_gods, CCGodsPage.class);
				config.addPage(R.string.tab_title_equipment, CCEquipmentPage.class);
				config.addPage(R.string.tab_title_map, MapViewPage.class);
				return config;
			}
		});
		configs.get(Series.CC).add(new BookConfig(R.drawable.tn_cc02) {
			@Override public AdventureConfig createAdventureConfig() {
				return null;
			}
		});
		configs.get(Series.CC).add(new BookConfig(R.drawable.tn_cc03) {
			@Override public AdventureConfig createAdventureConfig() {
				return null;
			}
		});
	}

	private static void addDFBooks() {
		configs.get(Series.DF).add(new BookConfig(R.drawable.tn_df01) {
			@Override public AdventureConfig createAdventureConfig() {
				AdventureConfig config = new AdventureConfig(R.string.df_01, R.string.df_01_title, DFAdventureActivity.class, DFCharacter.class, R.layout.activity_adventure);
				config.addCustomCharacterValues(Utils.getString(R.string.set_gold), 1);
				config.addCustomCharacterValues(Utils.getString(R.string.add_consumable_food), Utils.getString(R.string.food), 10);
				config.addDialog(PotionPicker.class, 2);
				config.addPage(R.string.tab_title_utilities, UtilitiesPage.class, getDFUtilitiesConfig());
				config.addPage(R.string.tab_title_combat, DFCombatPage.class);
				config.addPage(R.string.tab_title_items, ItemsPage.class);
				config.addPage(R.string.tab_title_map, MapViewPage.class);
				return config;
			}
		});
		configs.get(Series.DF).add(new BookConfig(R.drawable.tn_df02) {
			@Override public AdventureConfig createAdventureConfig() {
				AdventureConfig config = new AdventureConfig(R.string.df_02, R.string.df_02_title, DFAdventureActivity.class, DF02Character.class, R.layout.activity_adventure);
				config.addDialog(DF02SpellPicker.class);
				config.addPage(R.string.tab_title_utilities, UtilitiesPage.class, getDFUtilitiesConfig());
				config.addPage(R.string.tab_title_combat, DFCombatPage.class);
				config.addPage(R.string.tab_title_items, ItemsPage.class);
				config.addPage(R.string.tab_title_spells, DF02SpellsPage.class);
				config.addPage(R.string.tab_title_map, MapViewPage.class);
				config.setFreeAreaInflater(FreeAreaInflaterDF02.INSTANCE);
				return config;
			}
		});
		configs.get(Series.DF).add(new BookConfig(R.drawable.tn_df03) {
			@Override public AdventureConfig createAdventureConfig() {
				AdventureConfig config = new AdventureConfig(R.string.df_03, R.string.df_03_title, DFAdventureActivity.class, DF03Character.class, R.layout.activity_adventure);
				config.addCustomCharacterValues(Utils.getString(R.string.set_gold), 30);
				config.addCustomCharacterValues(Utils.getString(R.string.add_consumable_food), Utils.getString(R.string.food), 10);
				config.addDialog(PotionPicker.class, 2);
				config.addDialog(DF03MagicItemPicker.class);
				config.addPage(R.string.tab_title_utilities, UtilitiesPage.class, getDFUtilitiesConfig());
				config.addPage(R.string.tab_title_combat, DFCombatPage.class);
				config.addPage(R.string.tab_title_items, ItemsPage.class);
				config.addPage(R.string.tab_title_magic_items, DF03MagicItemsPage.class);
				config.addPage(R.string.tab_title_map, MapViewPage.class);
				return config;
			}
		});
		configs.get(Series.DF).add(new BookConfig(R.drawable.tn_df04) {
			@Override public AdventureConfig createAdventureConfig() {
				AdventureConfig config = new AdventureConfig(R.string.df_04, R.string.df_04_title, DF04AdventureActivity.class, DF04Character.class, R.layout.activity_adventure_df04);
				config.addPage(R.string.tab_title_utilities, UtilitiesPage.class, getDFUtilitiesConfig());
				config.addPage(R.string.tab_title_combat, DF04CombatPage.class, Collections.singleton(Tag.ALLOW_DROP));
				config.addPage(R.string.tab_title_items, DF04ItemsPage.class);
				config.addPage(R.string.tab_title_map, MapViewPage.class);
				return config;
			}
		});
		configs.get(Series.DF).add(new BookConfig(R.drawable.tn_df05) {
			@Override public AdventureConfig createAdventureConfig() {
				AdventureConfig config = new AdventureConfig(R.string.df_05, R.string.df_05_title, DFAdventureActivity.class, DFCharacter.class, R.layout.activity_adventure);
				config.addCustomCharacterValues(Utils.getString(R.string.set_gold), 30);
				config.addCustomCharacterValues(Utils.getString(R.string.add_consumable_food), Utils.getString(R.string.food), 10);
				config.addDialog(PotionPicker.class, 2);
				config.addPage(R.string.tab_title_utilities, UtilitiesPage.class, getDFUtilitiesConfig());
				config.addPage(R.string.tab_title_combat, DFCombatPage.class);
				config.addPage(R.string.tab_title_items, ItemsPage.class);
				config.addPage(R.string.tab_title_map, MapViewPage.class);
				return config;
			}
		});
		configs.get(Series.DF).add(new BookConfig(R.drawable.tn_df06) {
			@Override public AdventureConfig createAdventureConfig() {
				AdventureConfig config = new AdventureConfig(R.string.df_06, R.string.df_06_title, DFAdventureActivity.class, DFCharacter.class, R.layout.activity_adventure);
				config.addCustomCharacterValues(Utils.getString(R.string.add_consumable_food), Utils.getString(R.string.food), 10);
				config.addDialog(PotionPicker.class, 1);
				config.addPage(R.string.tab_title_utilities, UtilitiesPage.class, getDFUtilitiesConfig());
				config.addPage(R.string.tab_title_combat, DFCombatPage.class);
				config.addPage(R.string.tab_title_items, ItemsPage.class);
				config.addPage(R.string.tab_title_map, MapViewPage.class);
				return config;
			}
		});
		configs.get(Series.DF).add(new BookConfig(R.drawable.tn_df07) {
			@Override public AdventureConfig createAdventureConfig() {
				AdventureConfig config = new AdventureConfig(R.string.df_07, R.string.df_07_title, DFAdventureActivity.class, DFCharacter.class, R.layout.activity_adventure);
				config.addCustomCharacterValues(Utils.getString(R.string.add_consumable_food), Utils.getString(R.string.food), 10);
				config.addDialog(PotionPicker.class, 1);
				config.addPage(R.string.tab_title_utilities, UtilitiesPage.class, getDFUtilitiesConfig());
				config.addPage(R.string.tab_title_combat, DFCombatPage.class);
				config.addPage(R.string.tab_title_items, ItemsPage.class);
				config.addPage(R.string.tab_title_map, MapViewPage.class);
				return config;
			}
		});
		configs.get(Series.DF).add(new BookConfig(R.drawable.tn_df08) {
			@Override public AdventureConfig createAdventureConfig() {
				AdventureConfig config = new AdventureConfig(R.string.df_08, R.string.df_08_title, DFAdventureActivity.class, DF08Character.class, R.layout.activity_adventure);
				config.addPage(R.string.tab_title_utilities, UtilitiesPage.class, getDFUtilitiesConfig());
				config.addPage(R.string.tab_title_combat, DFCombatPage.class);
				config.addPage(R.string.tab_title_items, ItemsPage.class);
				config.addPage(R.string.tab_title_magic_stones, DF08MagicStonesPage.class);
				config.addPage(R.string.tab_title_map, MapViewPage.class);
				return config;
			}
		});
		configs.get(Series.DF).add(new BookConfig(R.drawable.tn_df09) {
			@Override public AdventureConfig createAdventureConfig() {
				AdventureConfig config = new AdventureConfig(R.string.df_09, R.string.df_09_title, DFAdventureActivity.class, DFCharacter.class, R.layout.activity_adventure);
				config.addCustomCharacterValues(Utils.getString(R.string.add_consumable_food), Utils.getString(R.string.food), 10);
				config.addDialog(PotionPicker.class, 1);
				config.addPage(R.string.tab_title_utilities, UtilitiesPage.class, getDFUtilitiesConfig());
				config.addPage(R.string.tab_title_combat, DFCombatPage.class);
				config.addPage(R.string.tab_title_items, ItemsPage.class);
				config.addPage(R.string.tab_title_map, MapViewPage.class);
				return config;
			}
		});
		configs.get(Series.DF).add(new BookConfig(R.drawable.tn_df10) {
			@Override public AdventureConfig createAdventureConfig() {
				AdventureConfig config = new AdventureConfig(R.string.df_10, R.string.df_10_title, DFAdventureActivity.class, DF10Character.class, R.layout.activity_adventure);
				config.addPage(R.string.tab_title_utilities, UtilitiesPage.class, getDFUtilitiesConfig());
				config.addPage(R.string.tab_title_combat, DFCombatPage.class);
				config.addPage(R.string.tab_title_items, ItemsPage.class);
				config.addPage(R.string.tab_title_map, MapViewPage.class);
				config.setFreeAreaInflater(FreeAreaInflaterDF10.INSTANCE);
				return config;
			}
		});
		configs.get(Series.DF).add(new BookConfig(R.drawable.tn_df11) {
			@Override public AdventureConfig createAdventureConfig() {
				AdventureConfig config = new AdventureConfig(R.string.df_11, R.string.df_11_title, DFAdventureActivity.class, DFCharacter.class, R.layout.activity_adventure);
				config.addCustomCharacterValues(Utils.getString(R.string.add_consumable_food), Utils.getString(R.string.food), 10);
				config.addDialog(PotionPicker.class, 1);
				config.addPage(R.string.tab_title_utilities, UtilitiesPage.class, getDFUtilitiesConfig());
				config.addPage(R.string.tab_title_combat, DFCombatPage.class);
				config.addPage(R.string.tab_title_items, ItemsPage.class);
				config.addPage(R.string.tab_title_map, MapViewPage.class);
				return config;
			}
		});
	}

	private static void addQGBooks() {
		configs.get(Series.QG).add(new BookConfig(R.drawable.tn_qg01) {
			@Override public AdventureConfig createAdventureConfig() {
				return null;
			}
		});
		configs.get(Series.QG).add(new BookConfig(R.drawable.tn_qg02) {
			@Override public AdventureConfig createAdventureConfig() {
				return null;
			}
		});
		configs.get(Series.QG).add(new BookConfig(R.drawable.tn_qg03) {
			@Override public AdventureConfig createAdventureConfig() {
				return null;
			}
		});
		configs.get(Series.QG).add(new BookConfig(R.drawable.tn_qg04) {
			@Override public AdventureConfig createAdventureConfig() {
				return null;
			}
		});
		configs.get(Series.QG).add(new BookConfig(R.drawable.tn_qg05) {
			@Override public AdventureConfig createAdventureConfig() {
				return null;
			}
		});
		configs.get(Series.QG).add(new BookConfig(R.drawable.tn_qg06) {
			@Override public AdventureConfig createAdventureConfig() {
				return null;
			}
		});
		configs.get(Series.QG).add(new BookConfig(R.drawable.tn_qg07) {
			@Override public AdventureConfig createAdventureConfig() {
				return null;
			}
		});
		configs.get(Series.QG).add(new BookConfig(R.drawable.tn_qg08) {
			@Override public AdventureConfig createAdventureConfig() {
				return null;
			}
		});
	}

	private static void addSOBooks() {
		configs.get(Series.SO).add(new BookConfig(R.drawable.tn_so01) {
			@Override public AdventureConfig createAdventureConfig() {
				return completeCommonSOConfig(R.string.so_01, R.string.so_01_title, 0);
			}
		});
		configs.get(Series.SO).add(new BookConfig(R.drawable.tn_so02) {
			@Override public AdventureConfig createAdventureConfig() {
				return completeCommonSOConfig(R.string.so_02, R.string.so_02_title, R.string.so_01);
			}
		});
		configs.get(Series.SO).add(new BookConfig(R.drawable.tn_so03) {
			@Override public AdventureConfig createAdventureConfig() {
				return completeCommonSOConfig(R.string.so_03, R.string.so_03_title, R.string.so_02);
			}
		});
		configs.get(Series.SO).add(new BookConfig(R.drawable.tn_so04) {
			@Override public AdventureConfig createAdventureConfig() {
				return completeCommonSOConfig(R.string.so_04, R.string.so_04_title, R.string.so_03);
			}
		});
	}

	private static AdventureConfig completeCommonSOConfig(int bookResId, int titleResId, int previousBookResId) {
		AdventureConfig config = new AdventureConfig(bookResId, titleResId, DFAdventureActivity.class, SOCharacter.class, R.layout.activity_adventure, previousBookResId);
		config.addCustomCharacterValues(Utils.getString(R.string.set_gold), 20);
		config.addCustomCharacterValues(Utils.getString(R.string.add_item), Utils.getString(R.string.food), 2);
		config.addPage(R.string.tab_title_utilities, UtilitiesPage.class, getDFUtilitiesConfig());
		config.addPage(R.string.tab_title_combat, DFCombatPage.class);
		config.addPage(R.string.tab_title_items, ItemsPage.class);
		config.addPage(R.string.tab_title_magic, SOSpellsPage.class);
		config.addPage(R.string.tab_title_map, MapViewPage.class);
		config.setFreeAreaInflater(FreeAreaInflaterSO.INSTANCE);
		return config;
	}

	private static Set<Tag> getCCUtilitiesConfig() {
		Set<Tag> config = new HashSet<>();
		config.add(Tag.UTIL_LAST_PARAGRAPH);
		config.add(Tag.UTIL_DICE);
		config.add(Tag.UTIL_ZEUS);
		return config;
	}

	private static Set<Tag> getDFUtilitiesConfig() {
		Set<Tag> config = new HashSet<>();
		config.add(Tag.UTIL_LAST_PARAGRAPH);
		config.add(Tag.UTIL_DICE);
		config.add(Tag.UTIL_LUCK_CHECK);
		return config;
	}
}
