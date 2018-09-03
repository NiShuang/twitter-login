

import net.dongliu.requests.RawResponse;
import net.dongliu.requests.Requests;
import net.dongliu.requests.Session;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ciel on 2018/9/3
 */

public class TwitterLogin {
    private Session session = Requests.session();
    private String username;
    private String password;

    public TwitterLogin(String username, String password) {
        this.username = username;
        this.password = password;
        this.login();
    }

    public void login(){
        String indexUrl = "https://mobile.twitter.com/login";
        String resp = session.get(indexUrl).timeout(15000).send().readToText();
        Pattern pattern = Pattern.compile("value=\"(.*?)\" name=\"authenticity_token\"");
        Matcher matcher =  pattern.matcher(resp);
        if(!matcher.find()) {
            pattern = Pattern.compile("name=\"authenticity_token\" type=\"hidden\" value=\"(.*?)\"");
            matcher = pattern.matcher(resp);
            if(!matcher.find()) return;
        }
        String authenticity_token = matcher.group(1);
        System.out.println(authenticity_token);
        String url = "https://mobile.twitter.com/sessions";
        Map<String, Object> params = new HashMap<>();
        params.put("session[username_or_email]", this.username);
        params.put("session[password]", this.password);
        params.put("remember_me", "1");
        params.put("wfa", "1");
        params.put("commit", "Log in");
        params.put("authenticity_token", authenticity_token);
        RawResponse resp1 = session.post(url).body(params).timeout(15000).send();
        System.out.println(resp1);
    }
}