package com.home.ldvelh.model.item;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Utils;
import com.home.ldvelh.model.value.ListValueHolder;

import java.util.Arrays;
import java.util.List;

public class CCGod extends Item {
    private static final long serialVersionUID = 6148810194174665308L;

    @SuppressWarnings("unused")
    public enum God {
        APHRODITE(R.string.cc_aphrodite),
        APOLLON(R.string.cc_apollon),
        ARES(R.string.cc_ares),
        ATHENA(R.string.cc_athena),
        HERA(R.string.cc_hera),
        POSEIDON(R.string.cc_poseidon),
        ASCLEPIOS(R.string.cc_asclepios),
        DEMETER(R.string.cc_demeter),
        DIONYSOS(R.string.cc_dionysos),
        ERIS(R.string.cc_eris),
        FURIES(R.string.cc_furies),
        HECATE(R.string.cc_hecate),
        HEPHAISTOS(R.string.cc_hephaistos),
		NO_GOD(R.string.empty_string);

        private final int nameResId;

        God(int nameResId) {
            this.nameResId = nameResId;
        }

        public int getNameResId() {
            return nameResId;
        }

		public static void populateList(ListValueHolder<CCGod> gods) {
			for (God god : values()) {
				if (god != NO_GOD) {
                    gods.add(new CCGod(Utils.getString(god.nameResId), god));
				}
			}
		}

        public static List<God> tutelaryGods() {
            return Arrays.asList(APHRODITE, APOLLON, ARES, ATHENA, HERA, POSEIDON);
        }
    }

    public enum Attitude {FAVORABLE, NEUTRAL, UNFAVORABLE}

    private Attitude attitude = Attitude.NEUTRAL;

    private CCGod(String name, Object data) {
        super(name, data);
    }

    @Override
    public boolean isIdentical(Item item) {
        return super.isIdentical(item) && attitude == ((CCGod) item).attitude;
    }

    public Attitude getAttitude() {
        return attitude;
    }

    public void setAttitude(Attitude attitude) {
        this.attitude = attitude;
    }
}
