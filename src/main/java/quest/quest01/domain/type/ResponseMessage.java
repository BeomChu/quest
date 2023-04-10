package quest.quest01.domain.type;

public enum ResponseMessage {

    BadRequest(400, "잘못된 요청입니다."),
    NotFoundItem(400, "등록되지 않은 상품 코드입니다."),
    FindItemCode(200, "교환 가능한 상품 코드입니다."),
    AlreadyChanged(400, "이미 교환된 상품 코드입니다."),
    SucClaim(200, "교환 성공"),
    HelpMessage(200, "사용법 안내");


    private final int code;
    private final String message;

    ResponseMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

    public String sayStoreCode(String storeCode) {
        return "교환된 상점 코드 : " + storeCode;
    }

    public String helpMessage(){
        StringBuffer sb = new StringBuffer();

        sb.append("사용법 안내입니다.");
        sb.append("");
        sb.append("상품 코드 교환 가능 여부 확인시 ");
        sb.append("CHECK [상품코드] ");
        sb.append("상품 교환 요청시 ");
        sb.append("CLAIM [상점코드] [상품코드] ");

        return sb.toString();
    }
}
