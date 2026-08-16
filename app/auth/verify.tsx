import { useEffect, useRef, useState } from "react";
import {
  View,
  Text,
  Pressable,
  TextInput,
  KeyboardAvoidingView,
  Platform,
  ScrollView,
  Animated,
  Easing,
  Image,
} from "react-native";
import { router, useLocalSearchParams } from "expo-router";
import { Ionicons } from "@expo/vector-icons";

export default function OTP() {
  const { mobile } = useLocalSearchParams();

  // =========================================
  // OTP STATE
  // =========================================

  const [otp, setOtp] = useState([
    "",
    "",
    "",
    "",
    "",
    "",
  ]);

  const [timer, setTimer] = useState(25);

  const [stage, setStage] = useState<
    "input" | "loading" | "success"
  >("input");

  const inputRefs = useRef<Array<TextInput | null>>([]);

  // =========================================
  // PAGE ANIMATION
  // =========================================

  const fadeAnim = useRef(
    new Animated.Value(0)
  ).current;

  const slideAnim = useRef(
    new Animated.Value(20)
  ).current;

  const illustrationAnim = useRef(
    new Animated.Value(0)
  ).current;

  const buttonScale = useRef(
    new Animated.Value(1)
  ).current;

  // =========================================
  // OTP BOX ANIMATION
  // =========================================

  const otpScales = useRef(
    Array.from(
      { length: 6 },
      () => new Animated.Value(1)
    )
  ).current;

  // =========================================
  // VERIFY ANIMATION
  // =========================================

  const boxesOpacity = useRef(
    new Animated.Value(1)
  ).current;

  const boxesScale = useRef(
    new Animated.Value(1)
  ).current;

  const successOpacity = useRef(
    new Animated.Value(0)
  ).current;

  const successScale = useRef(
    new Animated.Value(0.5)
  ).current;

  // =========================================
  // PAGE ENTRANCE ANIMATION
  // =========================================

  useEffect(() => {
    Animated.parallel([
      Animated.timing(fadeAnim, {
        toValue: 1,
        duration: 500,
        easing: Easing.out(Easing.cubic),
        useNativeDriver: true,
      }),

      Animated.timing(slideAnim, {
        toValue: 0,
        duration: 500,
        easing: Easing.out(Easing.cubic),
        useNativeDriver: true,
      }),
    ]).start();

    // Floating illustration
    Animated.loop(
      Animated.sequence([
        Animated.timing(illustrationAnim, {
          toValue: 1,
          duration: 1800,
          easing: Easing.inOut(Easing.sin),
          useNativeDriver: true,
        }),

        Animated.timing(illustrationAnim, {
          toValue: 0,
          duration: 1800,
          easing: Easing.inOut(Easing.sin),
          useNativeDriver: true,
        }),
      ])
    ).start();
  }, []);

  // =========================================
  // TIMER
  // =========================================

  useEffect(() => {
    if (timer <= 0) {
      return;
    }

    const interval = setInterval(() => {
      setTimer((previous) => {
        if (previous <= 1) {
          return 0;
        }

        return previous - 1;
      });
    }, 1000);

    return () => clearInterval(interval);
  }, [timer]);

  // =========================================
  // MOBILE NUMBER
  // =========================================

  const displayMobile =
    typeof mobile === "string" &&
    mobile.length > 0
      ? mobile
      : "9876543210";

  const maskedMobile =
    displayMobile.length >= 6
      ? `+91 ${displayMobile.slice(
          0,
          3
        )}${"*".repeat(
          Math.max(
            0,
            displayMobile.length - 6
          )
        )}${displayMobile.slice(-3)}`
      : "+91 9876543210";

  // =========================================
  // OTP BOX ANIMATION
  // =========================================

  const animateOTPBox = (index: number) => {
    Animated.sequence([
      Animated.spring(
        otpScales[index],
        {
          toValue: 1.1,
          friction: 4,
          tension: 120,
          useNativeDriver: true,
        }
      ),

      Animated.spring(
        otpScales[index],
        {
          toValue: 1,
          friction: 5,
          tension: 100,
          useNativeDriver: true,
        }
      ),
    ]).start();
  };

  // =========================================
  // OTP INPUT
  // =========================================

  const handleOTPChange = (
    value: string,
    index: number
  ) => {
    const numbers = value.replace(
      /[^0-9]/g,
      ""
    );

    const newOTP = [...otp];

    // =====================================
    // PASTE MULTIPLE DIGITS
    // =====================================

    if (numbers.length > 1) {
      const digits = numbers
        .slice(0, 6 - index)
        .split("");

      digits.forEach((digit, i) => {
        const position = index + i;

        if (position < 6) {
          newOTP[position] = digit;
          animateOTPBox(position);
        }
      });

      setOtp(newOTP);

      const nextIndex = Math.min(
        index + digits.length,
        5
      );

      inputRefs.current[
        nextIndex
      ]?.focus();

      return;
    }

    // =====================================
    // NORMAL INPUT
    // =====================================

    newOTP[index] = numbers;

    setOtp(newOTP);

    if (numbers) {
      animateOTPBox(index);

      if (index < 5) {
        inputRefs.current[
          index + 1
        ]?.focus();
      }
    }
  };

  // =========================================
  // BACKSPACE
  // =========================================

  const handleKeyPress = (
    event: any,
    index: number
  ) => {
    if (
      event.nativeEvent.key ===
        "Backspace" &&
      !otp[index] &&
      index > 0
    ) {
      inputRefs.current[
        index - 1
      ]?.focus();
    }
  };

  // =========================================
  // VERIFY OTP
  // =========================================

  const handleVerify = () => {
    const code = otp.join("");

    // Don't continue unless 6 digits
    if (code.length !== 6) {
      return;
    }

    console.log("OTP:", code);

    // =====================================
    // START LOADING
    // =====================================

    setStage("loading");

    // Hide OTP boxes
    Animated.parallel([
      Animated.timing(boxesOpacity, {
        toValue: 0,
        duration: 200,
        useNativeDriver: true,
      }),

      Animated.timing(boxesScale, {
        toValue: 0.8,
        duration: 200,
        useNativeDriver: true,
      }),
    ]).start();

    // =====================================
    // SHOW SUCCESS
    // =====================================

    setTimeout(() => {
      setStage("success");

      Animated.parallel([
        Animated.timing(successOpacity, {
          toValue: 1,
          duration: 250,
          useNativeDriver: true,
        }),

        Animated.spring(successScale, {
          toValue: 1,
          friction: 5,
          tension: 100,
          useNativeDriver: true,
        }),
      ]).start();

      // =================================
      // AUTOMATIC REDIRECT
      // =================================

      setTimeout(() => {
        console.log(
          "Redirecting to locationverify..."
        );

        router.replace(
          "./locationverify"
        );
      }, 700);
    }, 500);
  };

  // =========================================
  // VERIFY BUTTON PRESS
  // =========================================

  const pressVerify = () => {
    if (!allFilled) {
      return;
    }

    if (stage !== "input") {
      return;
    }

    Animated.sequence([
      Animated.timing(buttonScale, {
        toValue: 0.95,
        duration: 80,
        useNativeDriver: true,
      }),

      Animated.spring(buttonScale, {
        toValue: 1,
        friction: 5,
        tension: 100,
        useNativeDriver: true,
      }),
    ]).start();

    handleVerify();
  };

  // =========================================
  // RESEND OTP
  // =========================================

  const handleResend = () => {
    if (timer !== 0) {
      return;
    }

    const emptyOTP = [
      "",
      "",
      "",
      "",
      "",
      "",
    ];

    setOtp(emptyOTP);
    setTimer(25);
    setStage("input");

    // Reset animations
    boxesOpacity.setValue(1);
    boxesScale.setValue(1);
    successOpacity.setValue(0);
    successScale.setValue(0.5);

    inputRefs.current[0]?.focus();

    console.log("Resend OTP");

    // TODO:
    // Call your resend OTP API here
  };

  // =========================================
  // FLOATING IMAGE
  // =========================================

  const illustrationTranslateY =
    illustrationAnim.interpolate({
      inputRange: [0, 1],
      outputRange: [0, -8],
    });

  // =========================================
  // OTP STATUS
  // =========================================

  const allFilled = otp.every(
    (digit) => digit !== ""
  );

  // =========================================
  // UI
  // =========================================

  return (
    <KeyboardAvoidingView
      className="flex-1 bg-[#FFF8EF]"
      behavior={
        Platform.OS === "ios"
          ? "padding"
          : undefined
      }
    >
      <ScrollView
        showsVerticalScrollIndicator={false}
        keyboardShouldPersistTaps="handled"
        contentContainerStyle={{
          flexGrow: 1,
        }}
      >
        <View className="flex-1 px-6 pt-10">

          {/* ================================= */}
          {/* BACKGROUND DECORATION */}
          {/* ================================= */}

          <View
            pointerEvents="none"
            className="absolute"
            style={{
              top: -90,
              right: -80,
              width: 230,
              height: 230,
              borderRadius: 115,
              backgroundColor: "#F7D7C6",
              opacity: 0.5,
            }}
          />

          <View
            pointerEvents="none"
            className="absolute"
            style={{
              top: 250,
              left: -110,
              width: 190,
              height: 190,
              borderRadius: 95,
              backgroundColor: "#DCEBCF",
              opacity: 0.3,
            }}
          />

          <View
            pointerEvents="none"
            className="absolute"
            style={{
              bottom: 130,
              right: -50,
              width: 120,
              height: 120,
              borderRadius: 60,
              backgroundColor: "#F4C7B3",
              opacity: 0.18,
            }}
          />

          {/* Decorative dots */}

          <View
            pointerEvents="none"
            className="absolute right-16 top-32 w-2.5 h-2.5 rounded-full bg-[#C84A25]"
            style={{
              opacity: 0.5,
            }}
          />

          <View
            pointerEvents="none"
            className="absolute right-9 top-40 w-2 h-2 rounded-full bg-[#8FBF6A]"
          />

          <View
            pointerEvents="none"
            className="absolute left-10 top-60 w-1.5 h-1.5 rounded-full bg-[#C84A25]"
            style={{
              opacity: 0.4,
            }}
          />

          {/* ================================= */}
          {/* BACK BUTTON */}
          {/* ================================= */}

          <Pressable
            onPress={() => router.back()}
            className="
              w-12
              h-12
              rounded-full
              bg-white
              border
              border-[#EDE4D8]
              items-center
              justify-center
            "
            style={{
              shadowColor: "#000",
              shadowOpacity: 0.08,
              shadowRadius: 10,
              shadowOffset: {
                width: 0,
                height: 4,
              },
              elevation: 3,
            }}
          >
            <Ionicons
              name="arrow-back"
              size={24}
              color="#2A2A2A"
            />
          </Pressable>

          {/* ================================= */}
          {/* MAIN CONTENT */}
          {/* ================================= */}

          <Animated.View
            style={{
              opacity: fadeAnim,
              transform: [
                {
                  translateY: slideAnim,
                },
              ],
            }}
            className="items-center"
          >

            {/* ================================= */}
            {/* TITLE */}
            {/* ================================= */}

            <Text
              style={{
                fontFamily:
                  "Poppins_800ExtraBold",
              }}
              className="
                text-[#222222]
                text-[32px]
                mt-7
                text-center
              "
            >
              Verify your number
            </Text>

            <Text
              style={{
                fontFamily:
                  "Poppins_400Regular",
              }}
              className="
                text-[#6F6861]
                text-[15px]
                text-center
                mt-2
                leading-[22px]
              "
            >
              Enter the 6-digit code
              {"\n"}
              sent to
            </Text>

            <Text
              style={{
                fontFamily:
                  "Poppins_700Bold",
              }}
              className="
                text-[#3A342F]
                text-[16px]
                text-center
                mt-1
              "
            >
              {maskedMobile}
            </Text>

            {/* ================================= */}
            {/* SECURE BADGE */}
            {/* ================================= */}

            <View
              className="
                flex-row
                items-center
                bg-[#EDF6E8]
                px-4
                py-2.5
                rounded-full
                mt-4
                border
                border-[#D7E8CC]
              "
            >
              <View
                className="
                  w-7
                  h-7
                  rounded-full
                  bg-[#DCEBCF]
                  items-center
                  justify-center
                "
              >
                <Ionicons
                  name="shield-checkmark"
                  size={16}
                  color="#6D9E4E"
                />
              </View>

              <Text
                style={{
                  fontFamily:
                    "Poppins_600SemiBold",
                }}
                className="
                  text-[#5E8D43]
                  text-[13px]
                  ml-2
                "
              >
                Your OTP is secure
              </Text>
            </View>

            {/* ================================= */}
            {/* OTP AREA */}
            {/* ================================= */}

            <View
              style={{
                height: 75,
                width: "100%",
                alignItems: "center",
                justifyContent: "center",
                marginTop: 18,
              }}
            >

              {/* OTP INPUT BOXES */}

              {stage !== "success" && (
                <Animated.View
                  pointerEvents={
                    stage === "input"
                      ? "auto"
                      : "none"
                  }
                  style={{
                    position: "absolute",
                    flexDirection: "row",
                    justifyContent: "center",
                    opacity: boxesOpacity,
                    transform: [
                      {
                        scale: boxesScale,
                      },
                    ],
                  }}
                >
                  {otp.map(
                    (digit, index) => (
                      <Animated.View
                        key={index}
                        style={{
                          transform: [
                            {
                              scale:
                                otpScales[
                                  index
                                ],
                            },
                          ],
                        }}
                      >
                        <TextInput
                          ref={(ref) => {
                            inputRefs.current[
                              index
                            ] = ref;
                          }}
                          value={digit}
                          onChangeText={(
                            value
                          ) =>
                            handleOTPChange(
                              value,
                              index
                            )
                          }
                          onKeyPress={(
                            event
                          ) =>
                            handleKeyPress(
                              event,
                              index
                            )
                          }
                          keyboardType="number-pad"
                          maxLength={1}
                          selectTextOnFocus
                          textAlign="center"
                          editable={
                            stage === "input"
                          }
                          style={{
                            fontFamily:
                              "Poppins_700Bold",

                            shadowColor:
                              digit
                                ? "#C84A25"
                                : "#000",

                            shadowOpacity:
                              digit
                                ? 0.12
                                : 0.04,

                            shadowRadius:
                              digit
                                ? 9
                                : 5,

                            shadowOffset: {
                              width: 0,
                              height: 4,
                            },

                            elevation:
                              digit ? 4 : 2,
                          }}
                          className={`
                            w-[56px]
                            h-[66px]
                            rounded-[17px]
                            bg-white
                            border-[1.5px]
                            ${
                              digit
                                ? "border-[#C84A25]"
                                : "border-[#E4D9CC]"
                            }
                            text-[#2A2A2A]
                            text-[26px]
                            mx-[3px]
                          `}
                        />
                      </Animated.View>
                    )
                  )}
                </Animated.View>
              )}

              {/* ================================= */}
              {/* SUCCESS */}
              {/* ================================= */}

              {stage === "success" && (
                <Animated.View
                  style={{
                    opacity:
                      successOpacity,

                    transform: [
                      {
                        scale:
                          successScale,
                      },
                    ],

                    alignItems: "center",
                  }}
                >
                  <View
                    style={{
                      width: 72,
                      height: 72,
                      borderRadius: 36,
                      backgroundColor:
                        "#6D9E4E",

                      alignItems: "center",
                      justifyContent: "center",

                      shadowColor:
                        "#6D9E4E",

                      shadowOpacity: 0.3,
                      shadowRadius: 14,

                      shadowOffset: {
                        width: 0,
                        height: 6,
                      },

                      elevation: 7,
                    }}
                  >
                    <Ionicons
                      name="checkmark"
                      size={40}
                      color="#FFFFFF"
                    />
                  </View>

                  <Text
                    style={{
                      fontFamily:
                        "Poppins_700Bold",
                    }}
                    className="
                      text-[#3A342F]
                      text-[17px]
                      mt-3
                    "
                  >
                    You're Verified!
                  </Text>
                </Animated.View>
              )}
            </View>

            {/* ================================= */}
            {/* RESEND */}
            {/* ================================= */}

            {stage === "input" && (
              <View
                className="
                  flex-row
                  items-center
                  mt-3
                "
              >
                <Text
                  style={{
                    fontFamily:
                      "Poppins_400Regular",
                  }}
                  className="
                    text-[#6F6861]
                    text-[13px]
                  "
                >
                  Didn't receive the code?
                </Text>

                <Pressable
                  disabled={timer !== 0}
                  onPress={
                    handleResend
                  }
                >
                  <Text
                    style={{
                      fontFamily:
                        "Poppins_700Bold",
                    }}
                    className={`
                      text-[13px]
                      ml-1
                      ${
                        timer === 0
                          ? "text-[#C84A25]"
                          : "text-[#9B938A]"
                      }
                    `}
                  >
                    {timer === 0
                      ? "Resend OTP"
                      : `Resend in 00:${String(
                          timer
                        ).padStart(
                          2,
                          "0"
                        )}`}
                  </Text>
                </Pressable>
              </View>
            )}

            {/* ================================= */}
            {/* VERIFY BUTTON */}
            {/* ================================= */}

            {stage !== "success" && (
              <Animated.View
                style={{
                  transform: [
                    {
                      scale:
                        buttonScale,
                    },
                  ],
                }}
                className="w-full"
              >
                <Pressable
                  disabled={
                    !allFilled ||
                    stage ===
                      "loading"
                  }
                  onPress={
                    pressVerify
                  }
                  className={`
                    w-full
                    h-[60px]
                    rounded-full
                    items-center
                    justify-center
                    mt-5
                    ${
                      allFilled &&
                      stage === "input"
                        ? "bg-[#C84A25]"
                        : "bg-[#E5B9A8]"
                    }
                  `}
                  style={{
                    shadowColor:
                      allFilled &&
                      stage === "input"
                        ? "#C84A25"
                        : "transparent",

                    shadowOpacity: 0.32,
                    shadowRadius: 13,

                    shadowOffset: {
                      width: 0,
                      height: 6,
                    },

                    elevation:
                      allFilled &&
                      stage === "input"
                        ? 6
                        : 0,
                  }}
                >
                  <View className="flex-row items-center">

                    <Text
                      style={{
                        fontFamily:
                          "Poppins_700Bold",
                      }}
                      className="
                        text-white
                        text-[17px]
                      "
                    >
                      {stage ===
                      "loading"
                        ? "Verifying..."
                        : "Verify OTP"}
                    </Text>

                    {stage === "input" && (
                      <Ionicons
                        name="arrow-forward"
                        size={20}
                        color="#FFFFFF"
                        style={{
                          marginLeft: 9,
                        }}
                      />
                    )}

                    {stage ===
                      "loading" && (
                      <Ionicons
                        name="shield-checkmark"
                        size={19}
                        color="#FFFFFF"
                        style={{
                          marginLeft: 9,
                        }}
                      />
                    )}

                  </View>
                </Pressable>
              </Animated.View>
            )}
          </Animated.View>

          {/* ================================= */}
          {/* OTP ILLUSTRATION */}
          {/* ================================= */}

          <Animated.View
            pointerEvents="none"
            style={{
              transform: [
                {
                  translateY:
                    illustrationTranslateY,
                },
              ],
            }}
            className="
              items-center
              justify-center
              mt-2
              pb-2
            "
          >
            <Image
              source={require("../assets/images/otp.png")}
              style={{
                width: 260,
                height: 190,
              }}
              resizeMode="contain"
            />
          </Animated.View>

        </View>
      </ScrollView>
    </KeyboardAvoidingView>
  );
}