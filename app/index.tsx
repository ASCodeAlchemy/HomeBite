import React, { useEffect, useRef } from "react";
import {
  View,
  Image,
  Animated,
  Easing,
  Dimensions,
  StatusBar,
} from "react-native";
import { LinearGradient } from "expo-linear-gradient";
import { router } from "expo-router";

const homebiteLogo = require("./assets/images/homebite-logo.png");
const welcomeFood = require("./assets/images/welcome-food.png");

const { width, height } = Dimensions.get("window");

export default function WelcomeScreen() {
  // ==========================================
  // LOGO ANIMATION
  // ==========================================

  const logoOpacity = useRef(new Animated.Value(0)).current;
  const logoScale = useRef(new Animated.Value(0.8)).current;

  // ==========================================
  // TAGLINE
  // ==========================================

  const taglineOpacity = useRef(new Animated.Value(0)).current;

  // ==========================================
  // FOOD ANIMATION
  // ==========================================

  const foodOpacity = useRef(new Animated.Value(0)).current;
  const foodY = useRef(new Animated.Value(40)).current;
  const foodFloat = useRef(new Animated.Value(0)).current;

  // ==========================================
  // STEAM ANIMATION
  // ==========================================

  const steam1Opacity = useRef(new Animated.Value(0)).current;
  const steam1Y = useRef(new Animated.Value(15)).current;

  const steam2Opacity = useRef(new Animated.Value(0)).current;
  const steam2Y = useRef(new Animated.Value(15)).current;

  const steam3Opacity = useRef(new Animated.Value(0)).current;
  const steam3Y = useRef(new Animated.Value(15)).current;

  // ==========================================
  // BACKGROUND GLOW
  // ==========================================

  const glowScale = useRef(new Animated.Value(0.85)).current;
  const glowOpacity = useRef(new Animated.Value(0.25)).current;

  // ==========================================
  // BACKGROUND RINGS
  // ==========================================

  const ringScale = useRef(new Animated.Value(0.85)).current;
  const ringOpacity = useRef(new Animated.Value(0)).current;
  const ringRotate = useRef(new Animated.Value(0)).current;

  // ==========================================
  // DECORATIVE DOTS
  // ==========================================

  const dot1Y = useRef(new Animated.Value(0)).current;
  const dot2Y = useRef(new Animated.Value(0)).current;
  const dot3Y = useRef(new Animated.Value(0)).current;

  // ==========================================
  // FLOATING PARTICLES / LEAVES
  // ==========================================

  const particle1 = useRef(new Animated.Value(0)).current;
  const particle2 = useRef(new Animated.Value(0)).current;
  const particle3 = useRef(new Animated.Value(0)).current;
  const particle4 = useRef(new Animated.Value(0)).current;
  const particle5 = useRef(new Animated.Value(0)).current;
  const particle6 = useRef(new Animated.Value(0)).current;

  const particle1Opacity = useRef(new Animated.Value(0)).current;
  const particle2Opacity = useRef(new Animated.Value(0)).current;
  const particle3Opacity = useRef(new Animated.Value(0)).current;
  const particle4Opacity = useRef(new Animated.Value(0)).current;
  const particle5Opacity = useRef(new Animated.Value(0)).current;
  const particle6Opacity = useRef(new Animated.Value(0)).current;

  const particle1Rotate = useRef(new Animated.Value(0)).current;
  const particle2Rotate = useRef(new Animated.Value(0)).current;
  const particle3Rotate = useRef(new Animated.Value(0)).current;
  const particle4Rotate = useRef(new Animated.Value(0)).current;
  const particle5Rotate = useRef(new Animated.Value(0)).current;
  const particle6Rotate = useRef(new Animated.Value(0)).current;

  // ==========================================
  // STEAM ANIMATION FUNCTION
  // ==========================================

  const animateSteam = (
    opacity: Animated.Value,
    translateY: Animated.Value,
    delay: number
  ) => {
    Animated.loop(
      Animated.sequence([
        Animated.delay(delay),

        Animated.parallel([
          Animated.timing(opacity, {
            toValue: 0.7,
            duration: 400,
            useNativeDriver: true,
          }),

          Animated.timing(translateY, {
            toValue: -35,
            duration: 1800,
            easing: Easing.out(Easing.ease),
            useNativeDriver: true,
          }),
        ]),

        Animated.timing(opacity, {
          toValue: 0,
          duration: 500,
          useNativeDriver: true,
        }),

        Animated.timing(translateY, {
          toValue: 15,
          duration: 0,
          useNativeDriver: true,
        }),

        Animated.delay(300),
      ])
    ).start();
  };

  // ==========================================
  // PARTICLE ANIMATION FUNCTION
  // ==========================================

  const animateParticle = (
    movement: Animated.Value,
    opacity: Animated.Value,
    rotation: Animated.Value,
    delay: number
  ) => {
    Animated.loop(
      Animated.sequence([
        Animated.delay(delay),

        Animated.parallel([
          Animated.timing(opacity, {
            toValue: 0.8,
            duration: 400,
            useNativeDriver: true,
          }),

          Animated.timing(movement, {
            toValue: 1,
            duration: 3000,
            easing: Easing.out(Easing.cubic),
            useNativeDriver: true,
          }),

          Animated.timing(rotation, {
            toValue: 1,
            duration: 3000,
            easing: Easing.linear,
            useNativeDriver: true,
          }),
        ]),

        Animated.timing(opacity, {
          toValue: 0,
          duration: 700,
          useNativeDriver: true,
        }),

        Animated.parallel([
          Animated.timing(movement, {
            toValue: 0,
            duration: 0,
            useNativeDriver: true,
          }),

          Animated.timing(rotation, {
            toValue: 0,
            duration: 0,
            useNativeDriver: true,
          }),
        ]),

        Animated.delay(300),
      ])
    ).start();
  };

  // ==========================================
  // MAIN ANIMATION
  // ==========================================

  useEffect(() => {
    // ------------------------------------------
    // LOGO
    // ------------------------------------------

    Animated.parallel([
      Animated.timing(logoOpacity, {
        toValue: 1,
        duration: 900,
        easing: Easing.out(Easing.ease),
        useNativeDriver: true,
      }),

      Animated.spring(logoScale, {
        toValue: 1,
        friction: 7,
        tension: 45,
        useNativeDriver: true,
      }),
    ]).start();

    // ------------------------------------------
    // TAGLINE
    // ------------------------------------------

    Animated.timing(taglineOpacity, {
      toValue: 1,
      duration: 700,
      delay: 500,
      easing: Easing.out(Easing.ease),
      useNativeDriver: true,
    }).start();

    // ------------------------------------------
    // FOOD
    // ------------------------------------------

    Animated.parallel([
      Animated.timing(foodOpacity, {
        toValue: 1,
        duration: 900,
        delay: 500,
        easing: Easing.out(Easing.cubic),
        useNativeDriver: true,
      }),

      Animated.timing(foodY, {
        toValue: 0,
        duration: 900,
        delay: 500,
        easing: Easing.out(Easing.cubic),
        useNativeDriver: true,
      }),
    ]).start();

    // ------------------------------------------
    // STEAM
    // ------------------------------------------

    animateSteam(steam1Opacity, steam1Y, 0);
    animateSteam(steam2Opacity, steam2Y, 500);
    animateSteam(steam3Opacity, steam3Y, 1000);

    // ------------------------------------------
    // FOOD FLOAT
    // ------------------------------------------

    Animated.loop(
      Animated.sequence([
        Animated.timing(foodFloat, {
          toValue: -6,
          duration: 1800,
          easing: Easing.inOut(Easing.ease),
          useNativeDriver: true,
        }),

        Animated.timing(foodFloat, {
          toValue: 0,
          duration: 1800,
          easing: Easing.inOut(Easing.ease),
          useNativeDriver: true,
        }),
      ])
    ).start();

    // ==========================================
    // GLOW PULSE
    // ==========================================

    Animated.loop(
      Animated.sequence([
        Animated.parallel([
          Animated.timing(glowScale, {
            toValue: 1.08,
            duration: 1800,
            easing: Easing.inOut(Easing.ease),
            useNativeDriver: true,
          }),

          Animated.timing(glowOpacity, {
            toValue: 0.45,
            duration: 1800,
            easing: Easing.inOut(Easing.ease),
            useNativeDriver: true,
          }),
        ]),

        Animated.parallel([
          Animated.timing(glowScale, {
            toValue: 0.85,
            duration: 1800,
            easing: Easing.inOut(Easing.ease),
            useNativeDriver: true,
          }),

          Animated.timing(glowOpacity, {
            toValue: 0.25,
            duration: 1800,
            easing: Easing.inOut(Easing.ease),
            useNativeDriver: true,
          }),
        ]),
      ])
    ).start();

    // ==========================================
    // OUTER RING
    // ==========================================

    Animated.parallel([
      Animated.timing(ringOpacity, {
        toValue: 0.35,
        duration: 1200,
        delay: 500,
        useNativeDriver: true,
      }),

      Animated.loop(
        Animated.sequence([
          Animated.timing(ringScale, {
            toValue: 1.05,
            duration: 2200,
            easing: Easing.inOut(Easing.ease),
            useNativeDriver: true,
          }),

          Animated.timing(ringScale, {
            toValue: 0.85,
            duration: 2200,
            easing: Easing.inOut(Easing.ease),
            useNativeDriver: true,
          }),
        ])
      ),
    ]).start();

    // ==========================================
    // ROTATING RING
    // ==========================================

    Animated.loop(
      Animated.timing(ringRotate, {
        toValue: 1,
        duration: 10000,
        easing: Easing.linear,
        useNativeDriver: true,
      })
    ).start();

    // ==========================================
    // DECORATIVE DOT 1
    // ==========================================

    Animated.loop(
      Animated.sequence([
        Animated.timing(dot1Y, {
          toValue: -12,
          duration: 1600,
          easing: Easing.inOut(Easing.ease),
          useNativeDriver: true,
        }),

        Animated.timing(dot1Y, {
          toValue: 0,
          duration: 1600,
          easing: Easing.inOut(Easing.ease),
          useNativeDriver: true,
        }),
      ])
    ).start();

    // ==========================================
    // DECORATIVE DOT 2
    // ==========================================

    Animated.loop(
      Animated.sequence([
        Animated.timing(dot2Y, {
          toValue: 15,
          duration: 2000,
          easing: Easing.inOut(Easing.ease),
          useNativeDriver: true,
        }),

        Animated.timing(dot2Y, {
          toValue: 0,
          duration: 2000,
          easing: Easing.inOut(Easing.ease),
          useNativeDriver: true,
        }),
      ])
    ).start();

    // ==========================================
    // DECORATIVE DOT 3
    // ==========================================

    Animated.loop(
      Animated.sequence([
        Animated.timing(dot3Y, {
          toValue: -10,
          duration: 1800,
          easing: Easing.inOut(Easing.ease),
          useNativeDriver: true,
        }),

        Animated.timing(dot3Y, {
          toValue: 0,
          duration: 1800,
          easing: Easing.inOut(Easing.ease),
          useNativeDriver: true,
        }),
      ])
    ).start();

    // ==========================================
    // FLOATING PARTICLES
    // ==========================================

    animateParticle(
      particle1,
      particle1Opacity,
      particle1Rotate,
      0
    );

    animateParticle(
      particle2,
      particle2Opacity,
      particle2Rotate,
      500
    );

    animateParticle(
      particle3,
      particle3Opacity,
      particle3Rotate,
      1000
    );

    animateParticle(
      particle4,
      particle4Opacity,
      particle4Rotate,
      1500
    );

    animateParticle(
      particle5,
      particle5Opacity,
      particle5Rotate,
      2000
    );

    animateParticle(
      particle6,
      particle6Opacity,
      particle6Rotate,
      2500
    );

    // ==========================================
    // NAVIGATION
    // ==========================================

    const timer = setTimeout(() => {
      router.replace("/onboarding/one");
    }, 4000);

    return () => {
      clearTimeout(timer);
    };
  }, []);

  // ==========================================
  // RING ROTATION
  // ==========================================

  const rotate = ringRotate.interpolate({
    inputRange: [0, 1],
    outputRange: ["0deg", "360deg"],
  });

  // ==========================================
  // UI
  // ==========================================

  return (
    <LinearGradient
      colors={["#FFF8EF", "#FFEFDD", "#FCE4CC"]}
      start={{ x: 0.5, y: 0 }}
      end={{ x: 0.5, y: 1 }}
      className="flex-1 items-center justify-center"
    >
      <StatusBar
        barStyle="dark-content"
        backgroundColor="#FFF8EF"
      />

      {/* =====================================
          LARGE HOMEBITE LOGO
          ===================================== */}

      <Animated.View
        className="items-center justify-center"
        style={{
          width: width * 0.92,
          height: height * 0.30,
          opacity: logoOpacity,
          transform: [{ scale: logoScale }],
        }}
      >
        <Image
          source={homebiteLogo}
          className="h-full w-full"
          resizeMode="contain"
        />
      </Animated.View>

      {/* =====================================
          TAGLINE
          ===================================== */}

      <Animated.Text
        className="text-center text-[11px] font-bold tracking-[1.5px] text-[#C84A25]"
        style={{
          marginTop: -12,
          opacity: taglineOpacity,
        }}
      >
        ♥ HOMEMADE. MADE WITH LOVE ♥
      </Animated.Text>

      {/* =====================================
          FOOD ANIMATION AREA
          ===================================== */}

      <View
        className="items-center justify-center"
        style={{
          width: width * 0.95,
          height: height * 0.43,
          marginTop: height * 0.015,
        }}
      >

        {/* =====================================
            SOFT GLOW
            ===================================== */}

        <Animated.View
          style={{
            position: "absolute",
            width: width * 0.72,
            height: width * 0.72,
            borderRadius: width * 0.36,
            backgroundColor: "#F7A66D",
            opacity: glowOpacity,
            transform: [{ scale: glowScale }],
          }}
        />

        {/* =====================================
            OUTER ROTATING RING
            ===================================== */}

        <Animated.View
          style={{
            position: "absolute",
            width: width * 0.82,
            height: width * 0.82,
            borderRadius: width * 0.41,
            borderWidth: 2,
            borderColor: "#E8A06F",
            opacity: ringOpacity,
            transform: [
              { scale: ringScale },
              { rotate },
            ],
          }}
        />

        {/* =====================================
            INNER RING
            ===================================== */}

        <Animated.View
          style={{
            position: "absolute",
            width: width * 0.68,
            height: width * 0.68,
            borderRadius: width * 0.34,
            borderWidth: 1,
            borderColor: "#F0B98D",
            opacity: 0.35,
            transform: [{ scale: glowScale }],
          }}
        />

        {/* =====================================
            DECORATIVE DOT 1
            ===================================== */}

        <Animated.View
          style={{
            position: "absolute",
            left: width * 0.08,
            top: height * 0.12,
            width: 10,
            height: 10,
            borderRadius: 5,
            backgroundColor: "#E87845",
            opacity: 0.55,
            transform: [{ translateY: dot1Y }],
          }}
        />

        {/* =====================================
            DECORATIVE DOT 2
            ===================================== */}

        <Animated.View
          style={{
            position: "absolute",
            right: width * 0.08,
            top: height * 0.18,
            width: 7,
            height: 7,
            borderRadius: 4,
            backgroundColor: "#D98A5D",
            opacity: 0.45,
            transform: [{ translateY: dot2Y }],
          }}
        />

        {/* =====================================
            DECORATIVE DOT 3
            ===================================== */}

        <Animated.View
          style={{
            position: "absolute",
            left: width * 0.16,
            bottom: height * 0.07,
            width: 6,
            height: 6,
            borderRadius: 3,
            backgroundColor: "#C84A25",
            opacity: 0.4,
            transform: [{ translateY: dot3Y }],
          }}
        />

        {/* =====================================
            FLOATING LEAF 1
            ===================================== */}

        <Animated.Text
          style={{
            position: "absolute",
            left: width * 0.12,
            top: height * 0.18,
            fontSize: 20,
            opacity: particle1Opacity,
            zIndex: 1,
            transform: [
              {
                translateX: particle1.interpolate({
                  inputRange: [0, 1],
                  outputRange: [0, -45],
                }),
              },
              {
                translateY: particle1.interpolate({
                  inputRange: [0, 1],
                  outputRange: [0, -55],
                }),
              },
              {
                rotate: particle1Rotate.interpolate({
                  inputRange: [0, 1],
                  outputRange: ["0deg", "120deg"],
                }),
              },
            ],
          }}
        >
          🍃
        </Animated.Text>

        {/* =====================================
            PARTICLE 2
            ===================================== */}

        <Animated.View
          style={{
            position: "absolute",
            left: width * 0.20,
            top: height * 0.22,
            width: 7,
            height: 7,
            borderRadius: 4,
            backgroundColor: "#E87845",
            opacity: particle2Opacity,
            zIndex: 1,
            transform: [
              {
                translateX: particle2.interpolate({
                  inputRange: [0, 1],
                  outputRange: [0, -65],
                }),
              },
              {
                translateY: particle2.interpolate({
                  inputRange: [0, 1],
                  outputRange: [0, -20],
                }),
              },
            ],
          }}
        />

        {/* =====================================
            FLOATING LEAF 3
            ===================================== */}

        <Animated.Text
          style={{
            position: "absolute",
            right: width * 0.10,
            top: height * 0.18,
            fontSize: 18,
            opacity: particle3Opacity,
            zIndex: 1,
            transform: [
              {
                translateX: particle3.interpolate({
                  inputRange: [0, 1],
                  outputRange: [0, 50],
                }),
              },
              {
                translateY: particle3.interpolate({
                  inputRange: [0, 1],
                  outputRange: [0, -60],
                }),
              },
              {
                rotate: particle3Rotate.interpolate({
                  inputRange: [0, 1],
                  outputRange: ["0deg", "-100deg"],
                }),
              },
            ],
          }}
        >
          🍃
        </Animated.Text>

        {/* =====================================
            PARTICLE 4
            ===================================== */}

        <Animated.View
          style={{
            position: "absolute",
            right: width * 0.18,
            top: height * 0.25,
            width: 6,
            height: 6,
            borderRadius: 3,
            backgroundColor: "#F0A15F",
            opacity: particle4Opacity,
            zIndex: 1,
            transform: [
              {
                translateX: particle4.interpolate({
                  inputRange: [0, 1],
                  outputRange: [0, 65],
                }),
              },
              {
                translateY: particle4.interpolate({
                  inputRange: [0, 1],
                  outputRange: [0, -30],
                }),
              },
            ],
          }}
        />

        {/* =====================================
            FLOATING LEAF 5
            ===================================== */}

        <Animated.Text
          style={{
            position: "absolute",
            left: width * 0.18,
            bottom: height * 0.08,
            fontSize: 16,
            opacity: particle5Opacity,
            zIndex: 1,
            transform: [
              {
                translateX: particle5.interpolate({
                  inputRange: [0, 1],
                  outputRange: [0, -40],
                }),
              },
              {
                translateY: particle5.interpolate({
                  inputRange: [0, 1],
                  outputRange: [0, 50],
                }),
              },
              {
                rotate: particle5Rotate.interpolate({
                  inputRange: [0, 1],
                  outputRange: ["0deg", "180deg"],
                }),
              },
            ],
          }}
        >
          🍃
        </Animated.Text>

        {/* =====================================
            PARTICLE 6
            ===================================== */}

        <Animated.View
          style={{
            position: "absolute",
            right: width * 0.20,
            bottom: height * 0.10,
            width: 8,
            height: 8,
            borderRadius: 4,
            backgroundColor: "#C84A25",
            opacity: particle6Opacity,
            zIndex: 1,
            transform: [
              {
                translateX: particle6.interpolate({
                  inputRange: [0, 1],
                  outputRange: [0, 45],
                }),
              },
              {
                translateY: particle6.interpolate({
                  inputRange: [0, 1],
                  outputRange: [0, 45],
                }),
              },
            ],
          }}
        />

        {/* =====================================
            STEAM
            ===================================== */}

        <View
          className="items-center justify-center"
          style={{
            position: "absolute",
            top: height * 0.015,
            height: 65,
            width: 130,
            zIndex: 4,
          }}
        >
          <Animated.Text
            className="absolute text-[38px] font-light text-[#DCCFC3]"
            style={{
              opacity: steam1Opacity,
              transform: [
                { rotate: "90deg" },
                { translateY: steam1Y },
              ],
              marginLeft: -30,
            }}
          >
            〰
          </Animated.Text>

          <Animated.Text
            className="absolute text-[38px] font-light text-[#DCCFC3]"
            style={{
              opacity: steam2Opacity,
              transform: [
                { rotate: "90deg" },
                { translateY: steam2Y },
              ],
            }}
          >
            〰
          </Animated.Text>

          <Animated.Text
            className="absolute text-[38px] font-light text-[#DCCFC3]"
            style={{
              opacity: steam3Opacity,
              transform: [
                { rotate: "90deg" },
                { translateY: steam3Y },
              ],
              marginLeft: 30,
            }}
          >
            〰
          </Animated.Text>
        </View>

        {/* =====================================
            LARGE WELCOME FOOD
            ===================================== */}

        <Animated.View
          className="items-center justify-center"
          style={{
            width: width * 0.80,
            height: height * 0.38,
            marginTop: height * 0.035,
            opacity: foodOpacity,
            zIndex: 3,
            transform: [
              {
                translateY: Animated.add(
                  foodY,
                  foodFloat
                ),
              },
            ],
          }}
        >
          <Image
            source={welcomeFood}
            className="h-full w-full"
            resizeMode="contain"
          />
        </Animated.View>
      </View>
      <Animated.Text
  style={{
    marginTop: height * 0.015,
    fontSize: 15,
    fontWeight: "600",
    letterSpacing: 1.5,
    color: "#C84A25",
    opacity: taglineOpacity,
    textAlign: "center",
  }}
>
  Fresh  •  Homemade  •  Delicious
</Animated.Text>
    </LinearGradient>
  );
}