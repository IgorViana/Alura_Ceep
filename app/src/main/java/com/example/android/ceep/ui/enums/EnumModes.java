package com.example.android.ceep.ui.enums;

public enum EnumModes {
    MODE_LIST (1),
    MODE_GRID (2);

    private int valorMode;
    private static EnumModes[] vals = values();

    EnumModes(int valor) {
        valorMode = valor;
    }

    public int getMode() {
        return valorMode;
    }

    public static EnumModes buscaEnum (int codigo){
        for (EnumModes modes : values()){
            if(modes.getMode() == codigo){
                return modes;
            }
        }
        return  null;
    }

    public EnumModes move(){
        int index = ordinal();
        int nextIndex = index +1;
        nextIndex %= vals.length;
        return vals[nextIndex];
    }
}
