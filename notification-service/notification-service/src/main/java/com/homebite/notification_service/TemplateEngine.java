package com.homebite.notification_service;

import org.springframework.stereotype.Component;

@Component("emailTemplateEngine")
public class TemplateEngine {

    public String getRegisterOtpTemplate(String email, String otp) {
        StringBuilder otpBoxesHtml = new StringBuilder();
        if (otp != null) {
            for (char digit : otp.toCharArray()) {
                otpBoxesHtml.append(String.format("""
                        <span style="
                            display:inline-block;
                            width:52px;
                            height:52px;
                            line-height:52px;
                            margin:4px;
                            border:1px solid #e7d6cb;
                            border-radius:10px;
                            background:#ffffff;
                            font-size:30px;
                            font-weight:700;
                            color:#6b2400;
                            text-align:center;">%c</span>""", digit));
            }
        }

        return """
        <div style="background-color:#f5f2ef; padding:40px 16px; font-family:'Segoe UI', Arial, sans-serif;">
            <div style="max-width:620px; margin:0 auto; background-color:#ffffff; border-radius:20px; overflow:hidden; box-shadow:0 12px 30px rgba(0,0,0,0.08);">
        
                <div style="background-color:#6b2400; padding:40px 30px 70px; text-align:center;">
                    <div style="background:#ffffff; 
                                max-width:180px; 
                                margin:0 auto 25px;
                                padding:16px;
                                border-radius:16px; 
                                display:inline-block;
                                overflow:hidden;">
                        <img src="cid:logoImage" alt="HomeBite" style="width:100%%; height:auto; display:block; border:none;">
                    </div>
        
                    <p style="margin:0; color:#f8d9c7; letter-spacing:3px;
                              font-size:13px; font-weight:500;">
                        HOMEMADE. MADE WITH LOVE.
                    </p>
                </div>
        
                <div style="background:#ffffff;
                            margin-top:-35px;
                            border-radius:50px 50px 0 0;
                            padding:40px 40px 20px;">
        
                    <h1 style="margin:0 0 20px;
                               color:#4d240f;
                               font-size:38px;
                               font-weight:700;">
                        Hello, %s! 👋
                    </h1>
        
                    <p style="font-size:17px;
                              line-height:1.8;
                              color:#6b7280;">
                        Please verify your account by entering the code below into your
                        <strong style="color:#4d240f;">HomeBite</strong> account.
                        Use the code below to verify it's really you.
                        This code is valid for
                        <strong style="color:#c84f24;">10 minutes</strong> only.
                    </p>
        
                    <div style="
                        margin:35px 0;
                        border:2px dashed #c84f24;
                        border-radius:16px;
                        padding:30px;
                        text-align:center;
                        background:#fcf8f5;">
        
                        <p style="
                            margin:0 0 25px;
                            color:#c84f24;
                            letter-spacing:3px;
                            font-size:12px;
                            font-weight:700;">
                            YOUR ONE-TIME PASSWORD
                        </p>
        
                        <div style="text-align:center;">
                            %s
                        </div>
        
                        <p style="
                            margin-top:25px;
                            color:#9ca3af;
                            font-size:13px;">
                            Code expires in
                            <strong style="color:#c84f24;">10:00</strong>
                        </p>
        
                        <div style="
                            height:4px;
                            background:#c84f24;
                            margin-top:10px;
                            border-radius:20px;">
                        </div>
                    </div>
        
                    <div style="
                        background:#faf6f3;
                        border-left:5px solid #c84f24;
                        border-radius:10px;
                        padding:18px;
                        margin:25px 0;">
        
                        <p style="
                            margin:0;
                            color:#4d240f;
                            font-size:15px;
                            line-height:1.8;">
                            🔒 <strong>HomeBite will never call or message you for this code.</strong>
                            If you didn't request this login, simply ignore this email.
                            Your account remains secure.
                        </p>
                    </div>
        
                    <hr style="border:none;
                               border-top:1px solid #e5e7eb;
                               margin:35px 0;">
        
                    <p style="
                        color:#6b7280;
                        font-size:16px;
                        margin-bottom:10px;">
                        Hungry for home? We've got you covered. 🍲
                    </p>
        
                    <h3 style="
                        margin:0;
                        color:#4d240f;">
                        Team HomeBite
                    </h3>
        
                    <p style="color:#9ca3af; margin-top:8px;">
                        Connecting local chefs with hostelers
                    </p>
        
                </div>
        
                <div style="
                    background:#6b2400;
                    padding:30px;
                    text-align:center;">
        
                    <div style="margin-bottom:15px;">
                        <a href="#" style="color:#f8d9c7; text-decoration:none; margin:0 12px; font-size:13px;">Privacy Policy</a>
                        <a href="#" style="color:#f8d9c7; text-decoration:none; margin:0 12px; font-size:13px;">Help Center</a>
                        <a href="#" style="color:#f8d9c7; text-decoration:none; margin:0 12px; font-size:13px;">Unsubscribe</a>
                    </div>
        
                    <p style="color:#d6b6a4; font-size:12px; margin:10px 0 0;">
                        © 2026 HomeBite · Homemade. Made With Love.
                    </p>
        
                    <p style="color:#d6b6a4; font-size:11px; margin-top:8px;">
                        This email was sent because a login was attempted on your account.
                    </p>
                </div>
        
            </div>
        </div>
        """.formatted(email, otpBoxesHtml.toString());
    }

    public String getWelcomeTemplate(String email) {

        String username = email.split("@")[0];

        return """
```

<div style="background-color:#f5f2ef; padding:40px 16px; font-family:'Segoe UI', Arial, sans-serif;">
    <div style="max-width:620px; margin:0 auto; background-color:#ffffff;
                border-radius:20px; overflow:hidden;
                box-shadow:0 12px 30px rgba(0,0,0,0.08);">

```
    <!-- Header -->
    <div style="background-color:#6b2400; padding:40px 30px 70px; text-align:center;">
        <div style="background:#ffffff;
                    width:140px;
                    height:140px;
                    margin:0 auto 25px;
                    border-radius:16px;
                    display:flex;
                    align-items:center;
                    justify-content:center;">
            <img src="cid:logoImage" alt="HomeBite" style="width:100%%; height:auto; display:block; border:none;">
                 alt="HomeBite"
                 style="max-width:100px;">
        </div>

        <p style="margin:0;
                  color:#f8d9c7;
                  letter-spacing:3px;
                  font-size:13px;
                  font-weight:500;">
            HOMEMADE. MADE WITH LOVE.
        </p>
    </div>

    <!-- Main Content -->
    <div style="background:#ffffff;
                margin-top:-35px;
                border-radius:50px 50px 0 0;
                padding:40px;">

        <div style="text-align:center;">
            <div style="font-size:42px; margin-bottom:10px;">🎉</div>

            <h1 style="
                margin:0;
                color:#4d240f;
                font-size:38px;
                font-weight:700;">
                Welcome to HomeBite!
            </h1>

            <p style="
                margin-top:10px;
                color:#c84f24;
                font-size:17px;
                font-weight:600;">
                You're officially part of the family ❤️
            </p>
        </div>

        <hr style="
            border:none;
            border-top:1px solid #ececec;
            margin:35px 0;">

        <p style="
            font-size:18px;
            color:#4d240f;
            margin-bottom:25px;">
            Hey <strong>%s</strong> 👋,
        </p>

        <p style="
            color:#5f6368;
            line-height:1.9;
            font-size:16px;">
            Your account has been
            <strong style="color:#4d240f;">successfully created</strong>.
            We are thrilled to have you on board!
            HomeBite connects you with talented local chefs who cook
            fresh, home-style meals — just like mom used to make.
        </p>

        <!-- Feature Cards -->

        <div style="
            background:#faf6f3;
            border-left:5px solid #c84f24;
            border-radius:10px;
            padding:18px;
            margin-top:30px;">

            <h3 style="
                margin:0;
                color:#4d240f;
                font-size:18px;">
                👨‍🍳 Browse Local Chefs
            </h3>

            <p style="
                margin-top:8px;
                color:#7a7a7a;
                font-size:14px;">
                Discover home cooks near your hostel.
            </p>
        </div>

        <div style="
            background:#faf6f3;
            border-left:5px solid #c84f24;
            border-radius:10px;
            padding:18px;
            margin-top:15px;">

            <h3 style="
                margin:0;
                color:#4d240f;
                font-size:18px;">
                🍲 Order Fresh Meals Daily
            </h3>

            <p style="
                margin-top:8px;
                color:#7a7a7a;
                font-size:14px;">
                Get tiffin, thali, or à la carte meals delivered.
            </p>
        </div>

        <div style="
            background:#faf6f3;
            border-left:5px solid #c84f24;
            border-radius:10px;
            padding:18px;
            margin-top:15px;">

            <h3 style="
                margin:0;
                color:#4d240f;
                font-size:18px;">
                ⭐ Rate & Review Chefs
            </h3>

            <p style="
                margin-top:8px;
                color:#7a7a7a;
                font-size:14px;">
                Help the community discover the best homemade meals.
            </p>
        </div>

        <!-- CTA -->

        <div style="text-align:center; margin-top:35px;">

            <a href="http://localhost:5173/login"
               style="
                    background:#c84f24;
                    color:white;
                    text-decoration:none;
                    padding:16px 40px;
                    border-radius:30px;
                    display:inline-block;
                    font-size:18px;
                    font-weight:600;">
                🚀 Open HomeBite App
            </a>

            <p style="
                margin-top:18px;
                color:#8b949e;
                font-size:13px;">
                App not installed?
                <a href="#"
                   style="color:#c84f24;">
                    Download it here
                </a>
            </p>
        </div>

        <hr style="
            border:none;
            border-top:1px solid #ececec;
            margin:40px 0 25px;">

        <p style="
            color:#6b7280;
            margin-bottom:8px;">
            With love from our kitchen to yours ❤️
        </p>

        <h3 style="
            margin:0;
            color:#4d240f;">
            Team HomeBite
        </h3>

        <p style="
            margin-top:8px;
            color:#9ca3af;">
            Connecting local chefs with hostelers
        </p>

    </div>

    <!-- Footer -->

    <div style="
        background:#6b2400;
        padding:30px;
        text-align:center;">

        <div style="margin-bottom:15px;">

            <a href="#"
               style="color:#f8d9c7;
                      text-decoration:none;
                      margin:0 12px;
                      font-size:13px;">
                Privacy Policy
            </a>

            <a href="#"
               style="color:#f8d9c7;
                      text-decoration:none;
                      margin:0 12px;
                      font-size:13px;">
                Help Center
            </a>

            <a href="#"
               style="color:#f8d9c7;
                      text-decoration:none;
                      margin:0 12px;
                      font-size:13px;">
                Unsubscribe
            </a>
        </div>

        <p style="
            color:#d6b6a4;
            font-size:12px;
            margin:10px 0 0;">
            © %d HomeBite · Homemade. Made With Love.
        </p>

        <p style="
            color:#d6b6a4;
            font-size:11px;
            margin-top:8px;">
            You received this email because you created a HomeBite account.
        </p>
    </div>

</div>
```

</div>
""".formatted(
                username,
                java.time.Year.now().getValue()
        );
    }


    public String getLoginOtpTemplate(String email, String otp) {
        StringBuilder otpBoxesHtml = new StringBuilder();
        if (otp != null) {
            for (char digit : otp.toCharArray()) {
                otpBoxesHtml.append(String.format("""
                        <span style="
                            display:inline-block;
                            width:52px;
                            height:52px;
                            line-height:52px;
                            margin:4px;
                            border:1px solid #e7d6cb;
                            border-radius:10px;
                            background:#ffffff;
                            font-size:30px;
                            font-weight:700;
                            color:#6b2400;
                            text-align:center;">%c</span>""", digit));
            }
        }

        return """
        <div style="background-color:#f5f2ef; padding:40px 16px; font-family:'Segoe UI', Arial, sans-serif;">
            <div style="max-width:620px; margin:0 auto; background-color:#ffffff; border-radius:20px; overflow:hidden; box-shadow:0 12px 30px rgba(0,0,0,0.08);">
        
                <div style="background-color:#6b2400; padding:40px 30px 70px; text-align:center;">
                    <div style="background:#ffffff; 
                                max-width:180px; 
                                margin:0 auto 25px;
                                padding:16px;
                                border-radius:16px; 
                                display:inline-block;
                                overflow:hidden;">
                        <img src="cid:logoImage" alt="HomeBite" style="width:100%%; height:auto; display:block; border:none;">
                    </div>
        
                    <p style="margin:0; color:#f8d9c7; letter-spacing:3px;
                              font-size:13px; font-weight:500;">
                        HOMEMADE. MADE WITH LOVE.
                    </p>
                </div>
        
                <div style="background:#ffffff;
                            margin-top:-35px;
                            border-radius:50px 50px 0 0;
                            padding:40px 40px 20px;">
        
                    <h1 style="margin:0 0 20px;
                               color:#4d240f;
                               font-size:38px;
                               font-weight:700;">
                        Hello, %s! 👋
                    </h1>
        
                    <p style="font-size:17px;
                              line-height:1.8;
                              color:#6b7280;">
                        Please Login to your account by entering the code below into your
                        <strong style="color:#4d240f;">HomeBite</strong> account.
                        Use the code below to verify it's really you.
                        This code is valid for
                        <strong style="color:#c84f24;">10 minutes</strong> only.
                    </p>
        
                    <div style="
                        margin:35px 0;
                        border:2px dashed #c84f24;
                        border-radius:16px;
                        padding:30px;
                        text-align:center;
                        background:#fcf8f5;">
        
                        <p style="
                            margin:0 0 25px;
                            color:#c84f24;
                            letter-spacing:3px;
                            font-size:12px;
                            font-weight:700;">
                            YOUR ONE-TIME PASSWORD
                        </p>
        
                        <div style="text-align:center;">
                            %s
                        </div>
        
                        <p style="
                            margin-top:25px;
                            color:#9ca3af;
                            font-size:13px;">
                            Code expires in
                            <strong style="color:#c84f24;">10:00</strong>
                        </p>
        
                        <div style="
                            height:4px;
                            background:#c84f24;
                            margin-top:10px;
                            border-radius:20px;">
                        </div>
                    </div>
        
                    <div style="
                        background:#faf6f3;
                        border-left:5px solid #c84f24;
                        border-radius:10px;
                        padding:18px;
                        margin:25px 0;">
        
                        <p style="
                            margin:0;
                            color:#4d240f;
                            font-size:15px;
                            line-height:1.8;">
                            🔒 <strong>HomeBite will never call or message you for this code.</strong>
                            If you didn't request this login, simply ignore this email.
                            Your account remains secure.
                        </p>
                    </div>
        
                    <hr style="border:none;
                               border-top:1px solid #e5e7eb;
                               margin:35px 0;">
        
                    <p style="
                        color:#6b7280;
                        font-size:16px;
                        margin-bottom:10px;">
                        Hungry for home? We've got you covered. 🍲
                    </p>
        
                    <h3 style="
                        margin:0;
                        color:#4d240f;">
                        Team HomeBite
                    </h3>
        
                    <p style="color:#9ca3af; margin-top:8px;">
                        Connecting local chefs with hostelers
                    </p>
        
                </div>
        
                <div style="
                    background:#6b2400;
                    padding:30px;
                    text-align:center;">
        
                    <div style="margin-bottom:15px;">
                        <a href="#" style="color:#f8d9c7; text-decoration:none; margin:0 12px; font-size:13px;">Privacy Policy</a>
                        <a href="#" style="color:#f8d9c7; text-decoration:none; margin:0 12px; font-size:13px;">Help Center</a>
                        <a href="#" style="color:#f8d9c7; text-decoration:none; margin:0 12px; font-size:13px;">Unsubscribe</a>
                    </div>
        
                    <p style="color:#d6b6a4; font-size:12px; margin:10px 0 0;">
                        © 2026 HomeBite · Homemade. Made With Love.
                    </p>
        
                    <p style="color:#d6b6a4; font-size:11px; margin-top:8px;">
                        This email was sent because a login was attempted on your account.
                    </p>
                </div>
        
            </div>
        </div>
        """.formatted(email, otpBoxesHtml.toString());
    }






}