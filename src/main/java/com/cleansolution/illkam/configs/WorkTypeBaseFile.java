package com.cleansolution.illkam.configs;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkTypeBaseFile {
    // WorkTypeDim에 사용할 초기 데이터를 변수로 저장
    public static final List<Map<String, Object>> GENERAL_TYPE_DATA = Arrays.asList(
            new HashMap<String, Object>() {{
                put("name", "건물유형");
                put("inputType", "select");

                put("options", Arrays.asList(
                        "단독",
                        "다세대",
                        "빌라",
                        "오피스텔",
                        "아파트",
                        "상가",
                        "사무실",
                        "공장"
                ));
            }},
            new HashMap<String, Object>() {{
                put("name", "평수");
                put("inputType", "integer");
                put("placeholder", "평수를 입력해 주세요 예) 24평");
            }},
            new HashMap<String, Object>() {{
                put("name", "구조");
                put("placeholder", "구조를 입력해 주세요 예) 방3 / 화2 / 베1");
                put("inputType", "string");
            }}
    );

    public static final List<Map<String, Object>> AIRCON_TYPE_DATA = Arrays.asList(
            new HashMap<String, Object>() {{
                put("name", "제조사");
                put("inputType", "select");
                put("options", Arrays.asList(
                        "삼성",
                        "LG",
                        "위니아",
                        "캐리어",
                        "GE",
                        "기타"
                ));
            }},
            new HashMap<String, Object>() {{
                put("name", "종류");
                put("inputType", "select");
                put("options", Arrays.asList(
                        "1way",
                        "2way",
                        "4way",
                        "벽결이",
                        "스탠드",
                        "상업용스탠드",
                        "실외기"
                ));
            }},
            new HashMap<String, Object>() {{
                put("name", "대수");
                put("inputType", "integer");
                put("placeholder", "대수를 입력해 주세요 예) 2대");
            }}
    );
    public static final List<Map<String, Object>> ELEC_TYPE_DATA = Arrays.asList(
            new HashMap<String, Object>() {{
                put("name", "종류");
                put("inputType", "select");
                put("options", Arrays.asList(
                        "통돌이 세탁기",
                        "드럼 세탁기",
                        "단문 냉장고",
                        "양문 냉장고"
                ));
            }},
            new HashMap<String, Object>() {{
                put("name", "대수");
                put("inputType", "integer");
                put("placeholder", "대수를 입력해 주세요 예) 2대");
            }}
    );

    public static final List<Map<String, Object>> WINDOW_TYPE_DATA = Arrays.asList(
            new HashMap<String, Object>() {{
                put("name", "층수");
                put("inputType", "integer");
                put("placeholder", "층수를 입력해주세요 예) 3층 건물");
            }},
            new HashMap<String, Object>() {{
                put("name", "면적 및 면수");
                put("inputType", "string");
                put("placeholder", "면적 밑 면수를  입력해 주세요 예) 가로1M, 세로1.5M / 4면");
            }}
    );

    public static final List<Map<String, Object>> FABRIC_TYPE_DATA = Arrays.asList(
            new HashMap<String, Object>() {{
                put("name", "종류");
                put("placeholder", "종류를 입력해 주세요 예) 침대, 소파, 카펫트");
                put("inputType", "string");
            }}
    );

    public static final List<Map<String, Object>> INTERIOR_TYPE_DATA = Arrays.asList(
            new HashMap<String, Object>() {{
                put("name", "시공상세");
                put("inputType", "string");
            }}
    );

    public static final List<Map<String, Object>> AS_TYPE_DATA = Arrays.asList(

    );

    public static final List<Map<String, Object>> CRACK_TYPE_DATA = Arrays.asList(

    );

    public static final List<Map<String, Object>> TRASH_TYPE_DATA = Arrays.asList(
            new HashMap<String, Object>() {{
                put("name", "폐기물 양");
                put("inputType", "select");
                put("options", Arrays.asList(
                        "1톤 미만",
                        "1톤 이상 ~ 2.5톤 미만",
                        "2.5톤 이상 ~ 3.5톤 미만",
                        "3.5톤 이상",
                        "미정"
                ));
            }}
    );

    public static final List<Map<String, Object>> REQUEST_TYPE_DATA = Arrays.asList(
    );

    public static final List<Map<String, Object>> WORK_TYPES = Arrays.asList(
            new HashMap<String, Object>() {{
                put("name", "견적방문 요청");
                put("details", Arrays.asList(
                        "일반청소",
                        "에어컨",
                        "가전 청소",
                        "유리창 청소",
                        "패블릭 청소",
                        "인테리어 시공",
                        "A/S",
                        "철거",
                        "폐기물"
                ));
                put("workInfo", REQUEST_TYPE_DATA);
            }},
            new HashMap<String, Object>() {{
                put("name", "일반청소");
                put("details", Arrays.asList(
                        "이사청소",
                        "입주청소",
                        "사이청소",
                        "거주청소",
                        "준공청소",
                        "특수청소"
                ));
                put("workInfo", GENERAL_TYPE_DATA);
            }},
            new HashMap<String, Object>() {{
                put("name", "에어컨");
                put("details", Arrays.asList(
                        "청소",
                        "수리"
                ));
                put("workInfo", AIRCON_TYPE_DATA);
            }},
            new HashMap<String, Object>() {{
                put("name", "가전 청소");
                put("details", Arrays.asList(
                        "세탁기 청소",
                        "건조기 청소",
                        "냉장고 청소"
                ));
                put("workInfo", ELEC_TYPE_DATA);
            }},
            new HashMap<String, Object>() {{
                put("name", "유리창 청소");
                put("details", Arrays.asList(
                ));
                put("workInfo", WINDOW_TYPE_DATA);
            }},
            new HashMap<String, Object>() {{
                put("name", "패블릭 청소");
                put("details", Arrays.asList(
                ));
                put("workInfo", FABRIC_TYPE_DATA);
            }},
            new HashMap<String, Object>() {{
                put("name", "인테리어 시공");
                put("details", Arrays.asList(
                        "줄눈시공",
                        "상판코팅",
                        "탄성코트",
                        "방충망시공"
                ));
                put("workInfo", INTERIOR_TYPE_DATA);
            }},
            new HashMap<String, Object>() {{
                put("name", "A/S");
                put("details", Arrays.asList(
                        "일반청소",
                        "에어컨청소",
                        "가전청소",
                        "유리창청소",
                        "패블릭청소",
                        "인테리어시공"
                ));
                put("workInfo", AS_TYPE_DATA);
            }},
            new HashMap<String, Object>() {{
                put("name", "철거");
                put("details", Arrays.asList(
                        "인테리어 철거",
                        "원상복구 철거",
                        "바닥철거",
                        "비계 / 구조물 해체",
                        "코아전공",
                        "와이어 / 휠쏘 작업"
                ));
                put("workInfo", CRACK_TYPE_DATA);
            }},
            new HashMap<String, Object>() {{
                put("name", "폐기물");
                put("details", Arrays.asList(
                        "생활폐기물",
                        "건설폐기물",
                        "종합폐기물",
                        "기타폐기물"
                ));
                put("workInfo", TRASH_TYPE_DATA);
            }}

    );


}
