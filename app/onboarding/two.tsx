import { useEffect, useRef, useMemo } from "react";
import { View, Text, Pressable, Animated, Image, Easing, Dimensions } from "react-native";
import { router } from "expo-router";
import { Ionicons } from "@expo/vector-icons";
import { LinearGradient } from "expo-linear-gradient";

const { width: SCREEN_W } = Dimensions.get("window");

// ---------- Floating particle ----------
type ParticleConfig = {
  size: number;
  left: number;
  top: number;
  color: string;
  duration: number;
  delay: number;
  distance: number;
};

function Particle({ size, left, top, color, duration, delay, distance }: ParticleConfig) {
  const translateY = useRef(new Animated.Value(0)).current;
  const opacity = useRef(new Animated.Value(0)).current;

  useEffect(() => {
    const loop = Animated.loop(
      Animated.sequence([
        Animated.delay(delay),
        Animated.parallel([
          Animated.timing(opacity, {
            toValue: 1,
            duration: duration * 0.3,
            easing: Easing.out(Easing.quad),
            useNativeDriver: true,
          }),
          Animated.timing(translateY, {
            toValue: -distance,
            duration,
            easing: Easing.inOut(Easing.sin),
            useNativeDriver: true,
          }),
        ]),
        Animated.timing(opacity, {
          toValue: 0,
          duration: duration * 0.3,
          easing: Easing.in(Easing.quad),
          useNativeDriver: true,
        }),
        Animated.timing(translateY, {
          toValue: 0,
          duration: 0,
          useNativeDriver: true,
        }),
      ])
    );
    loop.start();
    return () => loop.stop();
  }, []);

  return (
    <Animated.View
      pointerEvents="none"
      style={{
        position: "absolute",
        left,
        top,
        width: size,
        height: size,
        borderRadius: size / 2,
        backgroundColor: color,
        opacity,
        transform: [{ translateY }],
      }}
    />
  );
}

function ParticleField() {
  const particles = useMemo<ParticleConfig[]>(
    () => [
      { size: 8, left: SCREEN_W * 0.12, top: 90, color: "#C84A25", duration: 3200, delay: 0, distance: 26 },
      { size: 5, left: SCREEN_W * 0.82, top: 130, color: "#F0B27A", duration: 2600, delay: 400, distance: 20 },
      { size: 6, left: SCREEN_W * 0.2, top: 260, color: "#E8C9A8", duration: 3600, delay: 800, distance: 30 },
      { size: 4, left: SCREEN_W * 0.75, top: 300, color: "#C84A25", duration: 2800, delay: 200, distance: 18 },
      { size: 7, left: SCREEN_W * 0.5, top: 70, color: "#F0B27A", duration: 3000, delay: 1000, distance: 24 },
      { size: 5, left: SCREEN_W * 0.9, top: 240, color: "#E8C9A8", duration: 2400, delay: 600, distance: 16 },
      { size: 6, left: SCREEN_W * 0.08, top: 340, color: "#C84A25", duration: 3400, delay: 1200, distance: 22 },
      { size: 4, left: SCREEN_W * 0.6, top: 380, color: "#F0B27A", duration: 2200, delay: 300, distance: 14 },
    ],
    []
  );

  return (
    <View pointerEvents="none" className="absolute inset-0">
      {particles.map((p, i) => (
        <Particle key={i} {...p} />
      ))}
    </View>
  );
}

// ---------- Main screen ----------
export default function OnboardingTwo() {
  const fadeHeading = useRef(new Animated.Value(0)).current;
  const slideHeading = useRef(new Animated.Value(24)).current;

  const fadeSubtitle = useRef(new Animated.Value(0)).current;
  const slideSubtitle = useRef(new Animated.Value(24)).current;

  const fadeImage = useRef(new Animated.Value(0)).current;
  const scaleImage = useRef(new Animated.Value(0.75)).current;
  const rotateImage = useRef(new Animated.Value(1)).current;

  const floatImage = useRef(new Animated.Value(0)).current;
  const glowPulse = useRef(new Animated.Value(0)).current;

  const fadeFooter = useRef(new Animated.Value(0)).current;
  const slideFooter = useRef(new Animated.Value(20)).current;

  const fadeBlobs = useRef(new Animated.Value(0)).current;

  useEffect(() => {
    Animated.timing(fadeBlobs, {
      toValue: 1,
      duration: 900,
      useNativeDriver: true,
    }).start();

    Animated.sequence([
      Animated.stagger(140, [
        Animated.parallel([
          Animated.timing(fadeHeading, {
            toValue: 1,
            duration: 550,
            easing: Easing.out(Easing.cubic),
            useNativeDriver: true,
          }),
          Animated.timing(slideHeading, {
            toValue: 0,
            duration: 550,
            easing: Easing.out(Easing.cubic),
            useNativeDriver: true,
          }),
        ]),
        Animated.parallel([
          Animated.timing(fadeSubtitle, {
            toValue: 1,
            duration: 500,
            easing: Easing.out(Easing.cubic),
            useNativeDriver: true,
          }),
          Animated.timing(slideSubtitle, {
            toValue: 0,
            duration: 500,
            easing: Easing.out(Easing.cubic),
            useNativeDriver: true,
          }),
        ]),
      ]),
      Animated.parallel([
        Animated.timing(fadeImage, {
          toValue: 1,
          duration: 500,
          useNativeDriver: true,
        }),
        Animated.spring(scaleImage, {
          toValue: 1,
          friction: 5,
          tension: 60,
          useNativeDriver: true,
        }),
        Animated.spring(rotateImage, {
          toValue: 0,
          friction: 6,
          tension: 50,
          useNativeDriver: true,
        }),
      ]),
      Animated.parallel([
        Animated.timing(fadeFooter, {
          toValue: 1,
          duration: 450,
          useNativeDriver: true,
        }),
        Animated.timing(slideFooter, {
          toValue: 0,
          duration: 450,
          useNativeDriver: true,
        }),
      ]),
    ]).start(() => {
      Animated.loop(
        Animated.sequence([
          Animated.timing(floatImage, {
            toValue: 1,
            duration: 1800,
            easing: Easing.inOut(Easing.sin),
            useNativeDriver: true,
          }),
          Animated.timing(floatImage, {
            toValue: 0,
            duration: 1800,
            easing: Easing.inOut(Easing.sin),
            useNativeDriver: true,
          }),
        ])
      ).start();

      Animated.loop(
        Animated.sequence([
          Animated.timing(glowPulse, {
            toValue: 1,
            duration: 2000,
            easing: Easing.inOut(Easing.sin),
            useNativeDriver: true,
          }),
          Animated.timing(glowPulse, {
            toValue: 0,
            duration: 2000,
            easing: Easing.inOut(Easing.sin),
            useNativeDriver: true,
          }),
        ])
      ).start();
    });
  }, []);

  const handleBack = () => {
    if (router.canGoBack()) {
      router.back();
    } else {
      router.replace("/");
    }
  };

  const handleSkip = () => {
    router.push("./one");
  };

  const handleNext = () => {
    router.push("./three");
  };

  const imageRotate = rotateImage.interpolate({
    inputRange: [-1, 0, 1],
    outputRange: ["-8deg", "0deg", "8deg"],
  });

  const imageFloatY = floatImage.interpolate({
    inputRange: [0, 1],
    outputRange: [0, -10],
  });

  const glowScale = glowPulse.interpolate({
    inputRange: [0, 1],
    outputRange: [1, 1.08],
  });

  const glowOpacity = glowPulse.interpolate({
    inputRange: [0, 1],
    outputRange: [0.35, 0.55],
  });

  return (
    <View className="flex-1 bg-[#FFF8EF]">
      {/* Background depth: soft gradient blobs */}
      <Animated.View style={{ opacity: fadeBlobs }} className="absolute inset-0">
        <View
          className="absolute -top-20 -right-24 w-64 h-64 rounded-full"
          style={{ backgroundColor: "#F6D9B8", opacity: 0.5 }}
        />
        <View
          className="absolute top-1/3 -left-20 w-48 h-48 rounded-full"
          style={{ backgroundColor: "#F0B27A", opacity: 0.25 }}
        />
        <View
          className="absolute bottom-0 -right-16 w-56 h-56 rounded-full"
          style={{ backgroundColor: "#E8C9A8", opacity: 0.3 }}
        />
        <LinearGradient
          colors={["rgba(255,248,239,0)", "#FFF8EF"]}
          style={{ position: "absolute", bottom: 0, left: 0, right: 0, height: 180 }}
        />
      </Animated.View>

      {/* Ambient particles */}
      <ParticleField />

      <View className="flex-1 px-6 pt-12 pb-8">
        {/* Back button */}
        <Pressable
          onPress={handleBack}
          className="w-10 h-10 rounded-full bg-white items-center justify-center"
          style={{
            shadowColor: "#000",
            shadowOpacity: 0.06,
            shadowRadius: 6,
            shadowOffset: { width: 0, height: 2 },
            elevation: 2,
          }}
        >
          <Ionicons name="chevron-back" size={22} color="#2A2A2A" />
        </Pressable>

        {/* Heading */}
        <Animated.View
          style={{ opacity: fadeHeading, transform: [{ translateY: slideHeading }] }}
          className="items-center mt-16"
        >
          <Text
            style={{ fontFamily: "Poppins_800ExtraBold" }}
            className="text-[42px] text-[#2A2A2A] text-center leading-[48px]"
          >
            Discover{"\n"}
            <Text style={{ fontFamily: "Poppins_800ExtraBold" }} className="text-[#C84A25]">
              Homemade
            </Text>{" "}
            Goodness
          </Text>
        </Animated.View>

        {/* Subtitle */}
        <Animated.View
          style={{ opacity: fadeSubtitle, transform: [{ translateY: slideSubtitle }] }}
          className="items-center mt-4"
        >
          <Text
            style={{ fontFamily: "Poppins_400Regular" }}
            className="text-[16px] text-[#8A8A8A] text-center leading-relaxed px-4"
          >
            Explore a variety of home-cooked meals made with love by local
            home chefs
          </Text>
        </Animated.View>

        {/* Illustration */}
        <View className="flex-1 items-center justify-center">
          {/* pulsing glow behind the image */}
          <Animated.View
            pointerEvents="none"
            style={{
              position: "absolute",
              width: 300,
              height: 300,
              borderRadius: 150,
              backgroundColor: "#F0B27A",
              opacity: glowOpacity,
              transform: [{ scale: glowScale }],
            }}
          />

          <Animated.View
            style={{
              opacity: fadeImage,
              transform: [
                { scale: scaleImage },
                { rotate: imageRotate },
                { translateY: imageFloatY },
              ],
            }}
          >
            <View
              className="w-72 h-72 rounded-full bg-white overflow-hidden items-center justify-center"
              style={{
                shadowColor: "#C84A25",
                shadowOpacity: 0.18,
                shadowRadius: 20,
                shadowOffset: { width: 0, height: 10 },
                elevation: 8,
              }}
            >
              <Image
                source={require("../assets/images/onboarding-one.png")}
                className="w-full h-full"
                resizeMode="cover"
              />
            </View>
          </Animated.View>
        </View>

        {/* Footer: dots + skip */}
        <Animated.View
          style={{ opacity: fadeFooter, transform: [{ translateY: slideFooter }] }}
          className="flex-row items-center justify-between pb-2"
        >
          <View className="flex-row items-center gap-1.5">
            <View className="h-1.5 w-4.5 rounded-full bg-[#C84A25]" />
            <View className="h-1.5 w-1.5 rounded-full bg-[#E5DFD8]" />
            <View className="h-1.5 w-1.5 rounded-full bg-[#E5DFD8]" />
          </View>

          <Pressable onPress={handleNext} className="flex-row items-center gap-1.5 active:opacity-70">
            <Text
              style={{ fontFamily: "Poppins_600SemiBold" }}
              className="text-[#2A2A2A] text-[15px]"
            >
              Skip
            </Text>
            <Ionicons name="arrow-forward" size={16} color="#2A2A2A" />
          </Pressable>
        </Animated.View>
      </View>
    </View>
  );
}