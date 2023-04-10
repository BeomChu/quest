package quest.quest01.util;


public class Script {
        public static String helpMessage(){
            StringBuffer sb = new StringBuffer();

            sb.append("<script>");
            sb.append("alert('상품 코드 교환 가능 여부 확인시');");
            sb.append("alert('CHECK [상품코드]');");
            sb.append("alert('상품 교환 요청시');");
            sb.append("alert('CLAIM [상점코드] [상품코드]');");
            sb.append("history.back();");
            sb.append("</script>");

            return sb.toString();
    }
}
