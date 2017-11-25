package com.home.ldvelh.model.combat;

public enum CCCondition {
    UNSCATHED {
        @Override
        public CCCondition worsen() { return WOUNDED; }

        @Override
        public int getColor() { return 0x77009900; }
    }, WOUNDED {
        @Override
        public CCCondition worsen() { return BADLY_WOUNDED; }

        @Override
        public int getColor() { return 0x77CCCC00; }
    }, BADLY_WOUNDED {
        @Override
        public CCCondition worsen() { return DEAD; }

        @Override
        public int getColor() { return 0x77CC3300; }
    }, DEAD {
        @Override
        public CCCondition worsen() { return DEAD; }

        @Override
        public int getColor() { return 0x77333300; }
    };

    public abstract CCCondition worsen();

    public abstract int getColor();
}
