import { useEffect, useRef } from "react";
import { View, Text, Pressable, Animated, Image } from "react-native";
import { router } from "expo-router";
import { Ionicons } from "@expo/vector-icons";

export default function Welcome() {
  const fadeLogo = useRef(new Animated.Value(0)).current;
  const scaleLogo = useRef(new Animated.Value(0.8)).current;

  const fadeHeading = useRef(new Animated.Value(0)).current;
  const slideHeading = useRef(new Animated.Value(20)).current;

  const fadeSubtitle = useRef(new Animated.Value(0)).current;
  const slideSubtitle = useRef(new Animated.Value(20)).current;

  const fadeImage = useRef(new Animated.Value(0)).current;
  const scaleImage = useRef(new Animated.Value(0.9)).current;

  const fadeFooter = useRef(new Animated.Value(0)).current;
  const slideFooter = useRef(new Animated.Value(20)).current;

  useEffect(() => {
    Animated.stagger(150, [
      Animated.parallel([
        Animated.timing(fadeLogo, { toValue: 1, duration: 500, useNativeDriver: true }),
        Animated.spring(scaleLogo, { toValue: 1, friction: 6, tension: 60, useNativeDriver: true }),
      ]),
      Animated.parallel([
        Animated.timing(fadeHeading, { toValue: 1, duration: 500, useNativeDriver: true }),
        Animated.timing(slideHeading, { toValue: 0, duration: 500, useNativeDriver: true }),
      ]),
      Animated.parallel([
        Animated.timing(fadeSubtitle, { toValue: 1, duration: 500, useNativeDriver: true }),
        Animated.timing(slideSubtitle, { toValue: 0, duration: 500, useNativeDriver: true }),
      ]),
      Animated.parallel([
        Animated.timing(fadeImage, { toValue: 1, duration: 600, useNativeDriver: true }),
        Animated.spring(scaleImage, { toValue: 1, friction: 6, tension: 60, useNativeDriver: true }),
      ]),
      Animated.parallel([
        Animated.timing(fadeFooter, { toValue: 1, duration: 500, useNativeDriver: true }),
        Animated.timing(slideFooter, { toValue: 0, duration: 500, useNativeDriver: true }),
      ]),
    ]).start();
  }, []);

  const handleBack = () => {
    if (router.canGoBack()) {
      router.back();
    } else {
      router.replace("/"); // adjust this path to your actual splash/entry route
    }
  };

  return (
    <View className="flex-1 bg-[#FFF8EF] px-6 pt-12 pb-8">
      {/* Back button */}
      <Pressable onPress={handleBack} className="w-10 h-10 justify-center">
        <Ionicons name="chevron-back" size={26} color="#2A2A2A" />
      </Pressable>

      {/* Logo */}
      <Animated.View
        style={{ opacity: fadeLogo, transform: [{ scale: scaleLogo }] }}
        className="items-center mt-1"
      >
        <Image
          source={require("../assets/images/homebite-logo.png")}
          className="w-72 h-32"
          resizeMode="contain"
        />
      </Animated.View>

      {/* Heading */}
      <Animated.View
        style={{ opacity: fadeHeading, transform: [{ translateY: slideHeading }] }}
        className="items-center mt-4"
      >
        <Text
          style={{ fontFamily: "Poppins_800ExtraBold" }}
          className="text-[38px] text-[#2A2A2A] text-center leading-tight"
        >
          Good food,
        </Text>
        <Text
          style={{ fontFamily: "Poppins_600SemiBold" }}
          className="text-[26px] text-[#2A2A2A] text-center leading-snug mt-1"
        >
          Made at home ❤️
        </Text>
      </Animated.View>

      {/* Subtitle */}
      <Animated.View
        style={{ opacity: fadeSubtitle, transform: [{ translateY: slideSubtitle }] }}
        className="items-center mt-3"
      >
        <Text
          style={{ fontFamily: "Poppins_400Regular" }}
          className="text-[17px] text-[#8A8A8A] text-center leading-relaxed px-4"
        >
          Delicious homemade meals from trusted home cooks, delivered to
          your doorstep.
        </Text>
      </Animated.View>

      {/* Illustration */}
      <Animated.View
        style={{ opacity: fadeImage, transform: [{ scale: scaleImage }] }}
        className="flex-1 justify-center items-center -mt-2"
      >
        <Image
          source={require("../assets/images/welcome-family.png")}
          className="w-full h-full"
          resizeMode="contain"
        />
      </Animated.View>

      {/* Footer: Get Started + Login */}
      <Animated.View style={{ opacity: fadeFooter, transform: [{ translateY: slideFooter }] }}>
        <Pressable
          onPress={() => router.push("/onboarding/two")}
          className="bg-[#C84A25] py-4 rounded-full items-center active:opacity-80"
        >
          <Text
            style={{ fontFamily: "Poppins_700Bold" }}
            className="text-white text-[17px]"
          >
            Get Started
          </Text>
        </Pressable>

        <Pressable
          //onPress={() => router.push("/auth/login")}
          className="mt-4 items-center"
        >
          <Text
            style={{ fontFamily: "Poppins_400Regular" }}
            className="text-[#8A8A8A] text-[15px]"
          >
           “Homemade goodness, just a tap away.” ❤️
          </Text>
        </Pressable>
      </Animated.View>
    </View>
  );
}