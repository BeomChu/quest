package quest.quest01.domain.type;

public enum Request {
    CHECK, HELP, CLAIM;

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
