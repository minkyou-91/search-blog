package com.searchblog.global.enums;

import lombok.Getter;

/**
 *  정렬 필드 구분값 매핑을 위해 사용
 */
public enum ParamCommand {
    SORT_TYPE_EXT("accuracy", "sim", "exact", "정확도순"),
    SORT_TYPE_LTT("recency", "date","latest", "최신순");

    @Getter private String kakaoSortVal;
    @Getter private String naverSortVal;
    @Getter private String inputSortVal;
    @Getter private String desc;

    ParamCommand(String kakaoSortVal, String naverSortVal, String inputSortVal, String desc) {
        this.kakaoSortVal = kakaoSortVal;
        this.naverSortVal = naverSortVal;
        this.inputSortVal = inputSortVal;
        this.desc = desc;
    }

    public static ParamCommand getParamCommand(final String inputSortVal){
        for(ParamCommand command : ParamCommand.values()){
            if(command.inputSortVal.equals(inputSortVal)){
                return command;
            }
        }
        return ParamCommand.SORT_TYPE_EXT; // 디폴트 : 정확도순 정렬
    }

}
