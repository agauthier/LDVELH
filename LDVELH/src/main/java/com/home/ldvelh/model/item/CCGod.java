package com.home.ldvelh.model.item;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Utils;
import com.home.ldvelh.model.value.ListValueHolder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CCGod extends Item {
    private static final long serialVersionUID = -6143386265862624217L;

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
                    gods.add(Item.create(CCGod.class, Utils.getString(god.nameResId), Collections.<Effect>emptyList(), god));
                }
            }
        }

        public static List<God> tutelaryGods() {
            return Arrays.asList(APHRODITE, APOLLON, ARES, ATHENA, HERA, POSEIDON);
        }
    }

    public enum Attitude {
        FAVORABLE(R.id.favorable), NEUTRAL(R.id.neutral), UNFAVORABLE(R.id.unfavorable);

        private final int buttonResId;

        Attitude(int buttonResId) {
            this.buttonResId = buttonResId;
        }
    }

    private Attitude attitude = Attitude.NEUTRAL;

    private CCGod() { super(); }

    @SuppressWarnings("unused")
    private CCGod(String name, List<Effect> effects, Object data) {
        super(name, effects, data);
    }

    @Override
    public <T extends Item> T copy() {
        CCGod god = new CCGod();
        populate(god);
        //noinspection unchecked
        return (T) god;
    }

    public Attitude getAttitude() {
        return attitude;
    }

    public void setAttitude(Attitude attitude) {
        this.attitude = attitude;
    }

    @Override
    protected void populate(Item item) {
        CCGod god = (CCGod) item;
        super.populate(god);
        god.attitude = attitude;
    }
}
