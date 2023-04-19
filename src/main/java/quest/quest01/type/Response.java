package quest.quest01.type;

import lombok.Getter;

@Getter
public enum Response {

    BAD_REQUEST_ITEM(400, "상품 코드는 숫자로 이루어진 9글자 입니다."),
    BAD_REQUEST_STORE(400, "상점 코드는 알파벳으로 이루어진 6글자 입니다."),
    INVALID(400, "잘못된 입력입니다. 입력 형식을 확인해 주세요."),
    NOT_FOUND_ITEM(404, "찾을 수 없는 상품입니다."),
    NOT_FOUND_CHANGER(403, "등록되지 않은 이용자 입니다."),
    NOT_FOUND_DRAWER(403, "등록되지 않은 이용자 입니다."),
    NOT_FOUND_DEPARTMENT(403, "등록되지 않은 부서 입니다."),
    YET_CHANGED(200, "교환 가능한 상품 코드입니다."),
    ALREADY_CHANGED(400, "이미 교환된 상품 코드입니다."),
    ALREADY_REGISTERED(400, "이미 등록된 부서입니다."),
    PARENT_ONLY_ONE(400,"상위 부서는 하나만 등록 가능합니다"),
    SUC_CLAIM(200, "상품이 교환되었습니다."),
    SUC_OFFER(200, "상품 코드 10개가 제공되었습니다."),
    HELP_MESSAGE(200, "사용법 안내");




    private final int code;
    private final String message;

    Response(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String sayStoreCode(String storeCode) {
        return "교환된 상점 코드 : " + storeCode;
    }

    public String helpMessage(){
        StringBuffer sb = new StringBuffer();

        sb.append("상품 코드 교환 가능 여부 확인시").append("%n");
        sb.append("CHECK [상품코드] ").append("%n");
        sb.append("상품 교환 요청시 ").append("%n");
        sb.append("CLAIM [상점코드] [상품코드] ").append("%n");

        return sb.toString();
    }
}
