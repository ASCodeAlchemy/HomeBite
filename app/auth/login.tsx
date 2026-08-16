import { useEffect, useRef, useState } from "react";
import {
  View,
  Text,
  Pressable,
  Animated,
  TextInput,
  Easing,
  KeyboardAvoidingView,
  Platform,
  Dimensions,
  ScrollView,
  Modal,
  Image,
  FlatList,
} from "react-native";
import { router } from "expo-router";
import { Ionicons, AntDesign } from "@expo/vector-icons";

const { width: SCREEN_W } = Dimensions.get("window");

const TAB_WIDTH = (SCREEN_W - 48) / 2;

type Tab = "mobile" | "email";

type Country = {
  name: string;
  code: string;
  flag: string;
};

export default function Login() {
  const [activeTab, setActiveTab] = useState<Tab>("mobile");
  const [mobile, setMobile] = useState("");
  const [email, setEmail] = useState("");
  const [focused, setFocused] = useState(false);

  // Country selector
  const [countryModal, setCountryModal] = useState(false);
  const [countrySearch, setCountrySearch] = useState("");

  const [selectedCountry, setSelectedCountry] = useState<Country>({
    name: "India",
    code: "+91",
    flag: "🇮🇳",
  });

  // =========================
  // COUNTRY LIST
  // =========================

  const countries: Country[] = [
    {
      name: "India",
      code: "+91",
      flag: "🇮🇳",
    },
    {
      name: "United States",
      code: "+1",
      flag: "🇺🇸",
    },
    {
      name: "United Kingdom",
      code: "+44",
      flag: "🇬🇧",
    },
    {
      name: "Canada",
      code: "+1",
      flag: "🇨🇦",
    },
    {
      name: "Australia",
      code: "+61",
      flag: "🇦🇺",
    },
    {
      name: "Germany",
      code: "+49",
      flag: "🇩🇪",
    },
    {
      name: "France",
      code: "+33",
      flag: "🇫🇷",
    },
    {
      name: "Singapore",
      code: "+65",
      flag: "🇸🇬",
    },
    {
      name: "United Arab Emirates",
      code: "+971",
      flag: "🇦🇪",
    },
    {
      name: "Saudi Arabia",
      code: "+966",
      flag: "🇸🇦",
    },
    {
      name: "Japan",
      code: "+81",
      flag: "🇯🇵",
    },
    {
      name: "China",
      code: "+86",
      flag: "🇨🇳",
    },
    {
      name: "Italy",
      code: "+39",
      flag: "🇮🇹",
    },
    {
      name: "Spain",
      code: "+34",
      flag: "🇪🇸",
    },
    {
      name: "Brazil",
      code: "+55",
      flag: "🇧🇷",
    },
    {
      name: "South Africa",
      code: "+27",
      flag: "🇿🇦",
    },
    {
      name: "New Zealand",
      code: "+64",
      flag: "🇳🇿",
    },
    {
      name: "Nepal",
      code: "+977",
      flag: "🇳🇵",
    },
    {
      name: "Bangladesh",
      code: "+880",
      flag: "🇧🇩",
    },
    {
      name: "Sri Lanka",
      code: "+94",
      flag: "🇱🇰",
    },
  ];

  // Search country
  const filteredCountries = countries.filter((country) =>
    country.name
      .toLowerCase()
      .includes(countrySearch.toLowerCase())
  );

  // =========================
  // ANIMATIONS
  // =========================

  const fadeHeader = useRef(new Animated.Value(0)).current;
  const slideHeader = useRef(new Animated.Value(20)).current;

  const fadeCard = useRef(new Animated.Value(0)).current;
  const slideCard = useRef(new Animated.Value(30)).current;

  const fadeFooter = useRef(new Animated.Value(0)).current;

  const tabIndicator = useRef(new Animated.Value(0)).current;

  const floatingLeaf = useRef(new Animated.Value(0)).current;

  // =========================
  // PAGE ANIMATION
  // =========================

  useEffect(() => {
    Animated.stagger(130, [
      Animated.parallel([
        Animated.timing(fadeHeader, {
          toValue: 1,
          duration: 550,
          easing: Easing.out(Easing.cubic),
          useNativeDriver: true,
        }),

        Animated.timing(slideHeader, {
          toValue: 0,
          duration: 550,
          easing: Easing.out(Easing.cubic),
          useNativeDriver: true,
        }),
      ]),

      Animated.parallel([
        Animated.timing(fadeCard, {
          toValue: 1,
          duration: 550,
          easing: Easing.out(Easing.cubic),
          useNativeDriver: true,
        }),

        Animated.timing(slideCard, {
          toValue: 0,
          duration: 550,
          easing: Easing.out(Easing.cubic),
          useNativeDriver: true,
        }),
      ]),

      Animated.timing(fadeFooter, {
        toValue: 1,
        duration: 500,
        useNativeDriver: true,
      }),
    ]).start();

    Animated.loop(
      Animated.sequence([
        Animated.timing(floatingLeaf, {
          toValue: 1,
          duration: 2200,
          easing: Easing.inOut(Easing.sin),
          useNativeDriver: true,
        }),

        Animated.timing(floatingLeaf, {
          toValue: 0,
          duration: 2200,
          easing: Easing.inOut(Easing.sin),
          useNativeDriver: true,
        }),
      ])
    ).start();
  }, []);

  // =========================
  // SWITCH TAB
  // =========================

  const switchTab = (tab: Tab) => {
    setActiveTab(tab);
    setFocused(false);

    Animated.timing(tabIndicator, {
      toValue: tab === "mobile" ? 0 : 1,
      duration: 260,
      easing: Easing.out(Easing.cubic),
      useNativeDriver: true,
    }).start();
  };

  // =========================
  // BACK
  // =========================

  const handleBack = () => {
    if (router.canGoBack()) {
      router.back();
    } else {
      router.replace("/");
    }
  };

  // =========================
  // CONTINUE
  // =========================

  const handleContinue = () => {
    // You can add validation here later.

    router.push("./verify");
  };

  // =========================
  // SIGN UP
  // =========================

  const handleSignup = () => {
    router.push("./signup");
  };

  // =========================
  // GOOGLE
  // =========================

  const handleGoogleSignIn = () => {
    // TODO: Google authentication
  };

  // =========================
  // APPLE
  // =========================

  const handleAppleSignIn = () => {
    // TODO: Apple authentication
  };

  // =========================
  // TAB INDICATOR
  // =========================

  const indicatorTranslateX = tabIndicator.interpolate({
    inputRange: [0, 1],
    outputRange: [0, TAB_WIDTH],
  });

  // =========================
  // LEAF ANIMATION
  // =========================

  const leafTranslateY = floatingLeaf.interpolate({
    inputRange: [0, 1],
    outputRange: [0, -10],
  });

  return (
    <KeyboardAvoidingView
      behavior={
        Platform.OS === "ios"
          ? "padding"
          : undefined
      }
      className="flex-1 bg-[#FFF8EF]"
    >
      <ScrollView
        showsVerticalScrollIndicator={false}
        keyboardShouldPersistTaps="handled"
        contentContainerStyle={{
          flexGrow: 1,
          paddingBottom: 35,
        }}
      >
        {/* ================= DECORATIVE BACKGROUND ================= */}

        <View
          pointerEvents="none"
          className="absolute"
          style={{
            top: -70,
            right: -70,
            width: 220,
            height: 220,
            borderRadius: 110,
            backgroundColor: "#F8D7C7",
            opacity: 0.35,
          }}
        />

        <View
          pointerEvents="none"
          className="absolute"
          style={{
            top: 150,
            left: -100,
            width: 180,
            height: 180,
            borderRadius: 90,
            backgroundColor: "#DCEBCF",
            opacity: 0.3,
          }}
        />

        {/* ================= FLOATING LEAF ================= */}

        <Animated.View
          pointerEvents="none"
          style={{
            position: "absolute",
            right: 35,
            top: 85,
            transform: [
              {
                translateY: leafTranslateY,
              },
              {
                rotate: "20deg",
              },
            ],
          }}
        >
          <Ionicons
            name="leaf"
            size={32}
            color="#8FBF6A"
          />
        </Animated.View>

        {/* Decorative dots */}

        <View
          pointerEvents="none"
          className="
            absolute
            right-20
            top-36
            w-2
            h-2
            rounded-full
            bg-[#C84A25]
            opacity-50
          "
        />

        <View
          pointerEvents="none"
          className="
            absolute
            right-12
            top-44
            w-1.5
            h-1.5
            rounded-full
            bg-[#8FBF6A]
          "
        />

        <View className="flex-1 px-6 pt-12">

          {/* ================= BACK BUTTON ================= */}

          <Pressable
            onPress={handleBack}
            className="
              w-11
              h-11
              rounded-full
              bg-white
              border
              border-[#EDE4D8]
              items-center
              justify-center
            "
            style={{
              shadowColor: "#000",
              shadowOpacity: 0.05,
              shadowRadius: 8,
              shadowOffset: {
                width: 0,
                height: 3,
              },
              elevation: 2,
            }}
          >
            <Ionicons
              name="chevron-back"
              size={27}
              color="#2A2A2A"
            />
          </Pressable>

          {/* ================= HEADER ================= */}

          <Animated.View
            style={{
              opacity: fadeHeader,
              transform: [
                {
                  translateY: slideHeader,
                },
              ],
            }}
            className="mt-8"
          >

            {/* Brand */}

            <View className="flex-row items-center mb-4">

              <View
  className="
    w-20
    h-20
    items-center
    justify-center
    mr-3
    overflow-hidden
  "
  style={{
    shadowColor: "#000",
    shadowOpacity: 0.08,
    shadowRadius: 6,
    shadowOffset: {
      width: 0,
      height: 2,
    },
    elevation: 2,
  }}
>
  <Image
    source={require("../assets/images/homebite-logo.png")}
    style={{
      width: 70,
      height: 70,
      resizeMode: "contain",
    }}
  />
</View>

              <Text
                style={{
                  fontFamily:
                    "Poppins_700Bold",
                }}
                className="
                  text-[#C84A25]
                  text-[25px]
                "
              >
                HomeBite
              </Text>

            </View>

            <Text
              style={{
                fontFamily:
                  "Poppins_800ExtraBold",
              }}
              className="
                text-[32px]
                leading-[40px]
                text-[#2A2A2A]
              "
            >
              Welcome Back! 👋
            </Text>

            <Text
              style={{
                fontFamily:
                  "Poppins_400Regular",
              }}
              className="
                text-[17px]
                text-[#8A8A8A]
                mt-2
              "
            >
              Login to continue to HomeBite
            </Text>

            {/* Secure badge */}

            <View
              className="
                self-start
                flex-row
                items-center
                bg-[#EDF6E8]
                px-3
                py-2
                rounded-full
                mt-5
              "
            >
              <Ionicons
                name="shield-checkmark"
                size={15}
                color="#6D9E4E"
              />

              <Text
                style={{
                  fontFamily:
                    "Poppins_500Medium",
                }}
                className="
                  text-[#6D9E4E]
                  text-[12px]
                  ml-1.5
                "
              >
                Safe & secure login
              </Text>
            </View>

          </Animated.View>

          {/* ================= LOGIN CARD ================= */}

          <Animated.View
            style={{
              opacity: fadeCard,
              transform: [
                {
                  translateY: slideCard,
                },
              ],
              shadowColor: "#8A5A43",
              shadowOpacity: 0.1,
              shadowRadius: 20,
              shadowOffset: {
                width: 0,
                height: 8,
              },
              elevation: 5,
            }}
            className="
              mt-8
              bg-white
              rounded-[28px]
              p-5
            "
          >

            {/* ================= TABS ================= */}

            <View
              className="
                flex-row
                border-b
                border-[#F0E8DE]
              "
            >

              <Pressable
                onPress={() =>
                  switchTab("mobile")
                }
                className="
                  flex-1
                  items-center
                  pb-4
                "
              >
                <Text
                  style={{
                    fontFamily:
                      activeTab === "mobile"
                        ? "Poppins_700Bold"
                        : "Poppins_400Regular",
                  }}
                  className={
                    activeTab === "mobile"
                      ? "text-[#2A2A2A] text-[16px]"
                      : "text-[#9B938A] text-[16px]"
                  }
                >
                  Mobile Number
                </Text>
              </Pressable>

              <Pressable
                onPress={() =>
                  switchTab("email")
                }
                className="
                  flex-1
                  items-center
                  pb-4
                "
              >
                <Text
                  style={{
                    fontFamily:
                      activeTab === "email"
                        ? "Poppins_700Bold"
                        : "Poppins_400Regular",
                  }}
                  className={
                    activeTab === "email"
                      ? "text-[#2A2A2A] text-[16px]"
                      : "text-[#9B938A] text-[16px]"
                  }
                >
                  Email
                </Text>
              </Pressable>

            </View>

            {/* ================= INDICATOR ================= */}

            <View className="h-[3px] bg-transparent">

              <Animated.View
                style={{
                  width: TAB_WIDTH - 40,
                  height: 3,
                  backgroundColor: "#C84A25",
                  borderRadius: 10,
                  transform: [
                    {
                      translateX:
                        indicatorTranslateX,
                    },
                  ],
                }}
              />

            </View>

            {/* ================= INPUT ================= */}

            <View className="mt-7">

              <Text
                style={{
                  fontFamily:
                    "Poppins_600SemiBold",
                }}
                className="
                  text-[#3A342F]
                  text-[14px]
                  mb-2
                "
              >
                {activeTab === "mobile"
                  ? "Mobile Number"
                  : "Email Address"}
              </Text>

              {/* ================= MOBILE ================= */}

              {activeTab === "mobile" ? (

                <View
                  className="
                    flex-row
                    items-center
                    rounded-2xl
                    px-4
                    h-[64px]
                  "
                  style={{
                    borderWidth: 1.5,
                    borderColor: focused
                      ? "#C84A25"
                      : "#EDE4D8",
                    backgroundColor: "#FFFCF9",
                  }}
                >

                  {/* COUNTRY SELECTOR */}

                  <Pressable
                    onPress={() => {
                      setCountryModal(true);
                      setCountrySearch("");
                    }}
                    className="
                      flex-row
                      items-center
                    "
                  >

                    <Text className="text-[22px]">
                      {selectedCountry.flag}
                    </Text>

                    <Text
                      style={{
                        fontFamily:
                          "Poppins_600SemiBold",
                      }}
                      className="
                        text-[#2A2A2A]
                        text-[16px]
                        ml-2
                      "
                    >
                      {selectedCountry.code}
                    </Text>

                    <Ionicons
                      name="chevron-down"
                      size={15}
                      color="#8A8A8A"
                      style={{
                        marginLeft: 5,
                        marginRight: 12,
                      }}
                    />

                  </Pressable>

                  {/* DIVIDER */}

                  <View
                    className="
                      w-[1px]
                      h-7
                      bg-[#EDE4D8]
                      mr-4
                    "
                  />

                  {/* MOBILE INPUT */}

                  <TextInput
                    value={mobile}
                    onChangeText={(text) => {
                      const numbers =
                        text.replace(
                          /[^0-9]/g,
                          ""
                        );

                      setMobile(
                        numbers.slice(0, 10)
                      );
                    }}
                    placeholder="Enter mobile number"
                    placeholderTextColor="#B8B0A6"
                    keyboardType="phone-pad"
                    maxLength={10}
                    onFocus={() =>
                      setFocused(true)
                    }
                    onBlur={() =>
                      setFocused(false)
                    }
                    style={{
                      fontFamily:
                        "Poppins_400Regular",
                      flex: 1,
                    }}
                    className="
                      text-[16px]
                      text-[#2A2A2A]
                    "
                  />

                </View>

              ) : (

                /* ================= EMAIL ================= */

                <View
                  className="
                    flex-row
                    items-center
                    rounded-2xl
                    px-4
                    h-[64px]
                  "
                  style={{
                    borderWidth: 1.5,
                    borderColor: focused
                      ? "#C84A25"
                      : "#EDE4D8",
                    backgroundColor: "#FFFCF9",
                  }}
                >

                  <Ionicons
                    name="mail-outline"
                    size={21}
                    color="#9B938A"
                    style={{
                      marginRight: 12,
                    }}
                  />

                  <TextInput
                    value={email}
                    onChangeText={setEmail}
                    placeholder="Enter your email"
                    placeholderTextColor="#B8B0A6"
                    keyboardType="email-address"
                    autoCapitalize="none"
                    autoCorrect={false}
                    onFocus={() =>
                      setFocused(true)
                    }
                    onBlur={() =>
                      setFocused(false)
                    }
                    style={{
                      fontFamily:
                        "Poppins_400Regular",
                      flex: 1,
                    }}
                    className="
                      text-[16px]
                      text-[#2A2A2A]
                    "
                  />

                </View>
              )}

            </View>

            {/* ================= CONTINUE ================= */}

            <Pressable
              onPress={handleContinue}
              className="
                bg-[#C84A25]
                h-[62px]
                rounded-full
                items-center
                justify-center
                mt-6
                active:opacity-80
              "
              style={{
                shadowColor: "#C84A25",
                shadowOpacity: 0.25,
                shadowRadius: 12,
                shadowOffset: {
                  width: 0,
                  height: 6,
                },
                elevation: 5,
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
                    text-[18px]
                  "
                >
                  Continue
                </Text>

                <Ionicons
                  name="arrow-forward"
                  size={20}
                  color="white"
                  style={{
                    marginLeft: 8,
                  }}
                />

              </View>

            </Pressable>

          </Animated.View>

          {/* ================= FOOTER ================= */}

          <Animated.View
            style={{
              opacity: fadeFooter,
            }}
            className="mt-9"
          >

            {/* Divider */}

            <View className="flex-row items-center">

              <View
                className="
                  flex-1
                  h-[1px]
                  bg-[#E5DCD1]
                "
              />

              <View
                className="
                  bg-[#FFF8EF]
                  px-4
                "
              >
                <Text
                  style={{
                    fontFamily:
                      "Poppins_400Regular",
                  }}
                  className="
                    text-[#9B938A]
                    text-[14px]
                  "
                >
                  or continue with
                </Text>
              </View>

              <View
                className="
                  flex-1
                  h-[1px]
                  bg-[#E5DCD1]
                "
              />

            </View>

            {/* Social Buttons */}

            <View
              className="
                flex-row
                justify-center
                gap-5
                mt-6
              "
            >

              <Pressable
                onPress={handleGoogleSignIn}
                className="
                  w-[62px]
                  h-[62px]
                  rounded-2xl
                  bg-white
                  border
                  border-[#EDE4D8]
                  items-center
                  justify-center
                  active:opacity-70
                "
              >
                <AntDesign
                  name="google"
                  size={25}
                  color="#2A2A2A"
                />
              </Pressable>

              <Pressable
                onPress={handleAppleSignIn}
                className="
                  w-[62px]
                  h-[62px]
                  rounded-2xl
                  bg-white
                  border
                  border-[#EDE4D8]
                  items-center
                  justify-center
                  active:opacity-70
                "
              >
                <AntDesign
                  name="apple"
                  size={27}
                  color="#2A2A2A"
                />
              </Pressable>

            </View>

            {/* ================= SIGN UP ================= */}

            <View
              className="
                flex-row
                items-center
                justify-center
                mt-7
              "
            >

              <Text
                style={{
                  fontFamily:
                    "Poppins_400Regular",
                }}
                className="
                  text-[#8A8A8A]
                  text-[14px]
                "
              >
                Don't have an account?
              </Text>

              <Pressable
                onPress={handleSignup}
                className="ml-1"
              >

                <Text
                  style={{
                    fontFamily:
                      "Poppins_700Bold",
                  }}
                  className="
                    text-[#C84A25]
                    text-[14px]
                  "
                >
                  Sign up now
                </Text>

              </Pressable>

            </View>

            {/* Terms */}

            <Text
              style={{
                fontFamily:
                  "Poppins_400Regular",
              }}
              className="
                text-[#9B938A]
                text-[13px]
                text-center
                leading-6
                mt-6
                px-5
              "
            >
              By continuing, you agree to our{" "}

              <Text
                style={{
                  fontFamily:
                    "Poppins_600SemiBold",
                }}
                className="text-[#2A2A2A]"
              >
                Terms & Conditions
              </Text>

              {" "}and{" "}

              <Text
                style={{
                  fontFamily:
                    "Poppins_600SemiBold",
                }}
                className="text-[#2A2A2A]"
              >
                Privacy Policy
              </Text>

            </Text>

          </Animated.View>

        </View>
      </ScrollView>

      {/* ===================================================== */}
      {/* COUNTRY SELECTOR MODAL */}
      {/* ===================================================== */}

      <Modal
        visible={countryModal}
        transparent
        animationType="slide"
        onRequestClose={() => {
          setCountryModal(false);
          setCountrySearch("");
        }}
      >

        <View
          className="
            flex-1
            justify-end
            bg-black/30
          "
        >

          <View
            className="
              bg-[#FFF8EF]
              rounded-t-[30px]
              px-6
              pt-5
              pb-7
            "
            style={{
              maxHeight: "80%",
            }}
          >

            {/* Modal Handle */}

            <View className="items-center mb-5">

              <View
                className="
                  w-12
                  h-1.5
                  bg-[#D8CEC2]
                  rounded-full
                "
              />

            </View>

            {/* Modal Header */}

            <View
              className="
                flex-row
                items-center
                justify-between
                mb-5
              "
            >

              <View>

                <Text
                  style={{
                    fontFamily:
                      "Poppins_700Bold",
                  }}
                  className="
                    text-[#2A2A2A]
                    text-[22px]
                  "
                >
                  Select country
                </Text>

                <Text
                  style={{
                    fontFamily:
                      "Poppins_400Regular",
                  }}
                  className="
                    text-[#9B938A]
                    text-[13px]
                    mt-1
                  "
                >
                  Choose your country code
                </Text>

              </View>

              <Pressable
                onPress={() => {
                  setCountryModal(false);
                  setCountrySearch("");
                }}
                className="
                  w-10
                  h-10
                  rounded-full
                  bg-white
                  items-center
                  justify-center
                "
              >

                <Ionicons
                  name="close"
                  size={22}
                  color="#2A2A2A"
                />

              </Pressable>

            </View>

            {/* Search */}

            <View
              className="
                flex-row
                items-center
                bg-white
                border
                border-[#EDE4D8]
                rounded-2xl
                px-4
                h-[56px]
                mb-4
              "
            >

              <Ionicons
                name="search-outline"
                size={21}
                color="#9B938A"
              />

              <TextInput
                value={countrySearch}
                onChangeText={setCountrySearch}
                placeholder="Search country"
                placeholderTextColor="#B8B0A6"
                style={{
                  fontFamily:
                    "Poppins_400Regular",
                }}
                className="
                  flex-1
                  ml-3
                  text-[15px]
                  text-[#2A2A2A]
                "
              />

              {countrySearch.length > 0 && (
                <Pressable
                  onPress={() =>
                    setCountrySearch("")
                  }
                >
                  <Ionicons
                    name="close-circle"
                    size={20}
                    color="#B8B0A6"
                  />
                </Pressable>
              )}

            </View>

            {/* Country List */}

            <FlatList
              data={filteredCountries}
              keyExtractor={(item) =>
                `${item.name}-${item.code}`
              }
              showsVerticalScrollIndicator={false}
              keyboardShouldPersistTaps="handled"
              renderItem={({ item }) => {

                const isSelected =
                  selectedCountry.name ===
                  item.name;

                return (
                  <Pressable
                    onPress={() => {
                      setSelectedCountry(item);
                      setCountryModal(false);
                      setCountrySearch("");
                    }}
                    className="
                      flex-row
                      items-center
                      py-4
                      px-3
                      rounded-2xl
                      mb-1
                    "
                    style={{
                      backgroundColor:
                        isSelected
                          ? "#FBE9E2"
                          : "transparent",
                    }}
                  >

                    {/* Flag */}

                    <Text className="text-[25px]">
                      {item.flag}
                    </Text>

                    {/* Country Name */}

                    <Text
                      style={{
                        fontFamily:
                          isSelected
                            ? "Poppins_600SemiBold"
                            : "Poppins_400Regular",
                      }}
                      className="
                        flex-1
                        ml-4
                        text-[#2A2A2A]
                        text-[15px]
                      "
                    >
                      {item.name}
                    </Text>

                    {/* Code */}

                    <Text
                      style={{
                        fontFamily:
                          "Poppins_600SemiBold",
                      }}
                      className="
                        text-[#8A8A8A]
                        text-[14px]
                      "
                    >
                      {item.code}
                    </Text>

                    {/* Selected */}

                    {isSelected && (
                      <Ionicons
                        name="checkmark-circle"
                        size={21}
                        color="#C84A25"
                        style={{
                          marginLeft: 10,
                        }}
                      />
                    )}

                  </Pressable>
                );
              }}
            />

          </View>

        </View>

      </Modal>

    </KeyboardAvoidingView>
  );
}