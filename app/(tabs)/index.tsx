import React from "react";
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  Pressable,
  StatusBar,
} from "react-native";

export default function HomeScreen() {
  return (
    <View style={styles.container}>
      <StatusBar
        barStyle="dark-content"
        backgroundColor="#FFF8EF"
      />

      <ScrollView
        showsVerticalScrollIndicator={false}
        contentContainerStyle={styles.content}
      >
        <View style={styles.header}>
          <View>
            <Text style={styles.greeting}>
              Good Morning 👋
            </Text>

            <Text style={styles.heading}>
              What are you craving?
            </Text>
          </View>a

          <Pressable style={styles.profileButton}>
            <Text style={styles.profileIcon}>👤</Text>
          </Pressable>
        </View>

        <View style={styles.banner}>
          <View style={styles.bannerText}>
            <Text style={styles.bannerSmall}>
              HOMEMADE WITH LOVE
            </Text>

            <Text style={styles.bannerTitle}>
              Taste the{"\n"}comfort of home
            </Text>

            <Text style={styles.bannerDescription}>
              Fresh homemade meals prepared by
              cooks near you.
            </Text>

            <Pressable style={styles.exploreButton}>
              <Text style={styles.exploreText}>
                Explore Meals
              </Text>
            </Pressable>
          </View>

          <Text style={styles.foodEmoji}>🍛</Text>
        </View>

        <View style={styles.sectionHeader}>
          <Text style={styles.sectionTitle}>
            Categories
          </Text>

          <Text style={styles.seeAll}>
            See all
          </Text>
        </View>

        <ScrollView
          horizontal
          showsHorizontalScrollIndicator={false}
          contentContainerStyle={styles.categories}
        >
          <Category emoji="🍛" title="Thali" />
          <Category emoji="🍚" title="Rice" />
          <Category emoji="🥘" title="Curries" />
          <Category emoji="🫓" title="Roti" />
          <Category emoji="🥗" title="Healthy" />
        </ScrollView>

        <View style={styles.sectionHeader}>
          <Text style={styles.sectionTitle}>
            Popular Meals
          </Text>

          <Text style={styles.seeAll}>
            See all
          </Text>
        </View>

        <MealCard
          emoji="🍛"
          name="Homestyle Veg Thali"
          description="Dal, rice, roti, sabzi & salad"
          price="₹120"
          rating="4.8"
        />

        <MealCard
          emoji="🥘"
          name="Paneer Masala"
          description="Homemade paneer with fresh spices"
          price="₹100"
          rating="4.7"
        />

        <MealCard
          emoji="🍚"
          name="Dal Khichdi"
          description="Comforting homemade dal khichdi"
          price="₹80"
          rating="4.9"
        />
      </ScrollView>
    </View>
  );
}

function Category({
  emoji,
  title,
}: {
  emoji: string;
  title: string;
}) {
  return (
    <Pressable style={styles.category}>
      <Text style={styles.categoryEmoji}>
        {emoji}
      </Text>

      <Text style={styles.categoryTitle}>
        {title}
      </Text>
    </Pressable>
  );
}

function MealCard({
  emoji,
  name,
  description,
  price,
  rating,
}: {
  emoji: string;
  name: string;
  description: string;
  price: string;
  rating: string;
}) {
  return (
    <Pressable style={styles.mealCard}>
      <View style={styles.mealImage}>
        <Text style={styles.mealEmoji}>
          {emoji}
        </Text>
      </View>

      <View style={styles.mealInfo}>
        <Text style={styles.mealName}>
          {name}
        </Text>

        <Text style={styles.mealDescription}>
          {description}
        </Text>

        <View style={styles.mealBottom}>
          <Text style={styles.rating}>
            ★ {rating}
          </Text>

          <Text style={styles.price}>
            {price}
          </Text>
        </View>
      </View>

      <Pressable style={styles.addButton}>
        <Text style={styles.addButtonText}>
          +
        </Text>
      </Pressable>
    </Pressable>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#FFF8EF",
  },

  content: {
    padding: 20,
    paddingBottom: 40,
  },

  header: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: 20,
  },

  greeting: {
    fontSize: 14,
    color: "#8A6A5A",
    marginBottom: 5,
  },

  heading: {
    fontSize: 22,
    fontWeight: "800",
    color: "#542719",
  },

  profileButton: {
    width: 45,
    height: 45,
    borderRadius: 23,
    backgroundColor: "#FFFFFF",
    justifyContent: "center",
    alignItems: "center",
    borderWidth: 1,
    borderColor: "#EBDDD0",
  },

  profileIcon: {
    fontSize: 21,
  },

  banner: {
    minHeight: 190,
    borderRadius: 24,
    backgroundColor: "#C84A25",
    padding: 22,
    overflow: "hidden",
    flexDirection: "row",
  },

  bannerText: {
    flex: 1,
    zIndex: 2,
  },

  bannerSmall: {
    color: "#FFE8D8",
    fontSize: 10,
    fontWeight: "800",
    letterSpacing: 1,
  },

  bannerTitle: {
    color: "#FFFFFF",
    fontSize: 26,
    fontWeight: "800",
    lineHeight: 31,
    marginTop: 8,
  },

  bannerDescription: {
    color: "#FFEFE7",
    fontSize: 12,
    lineHeight: 17,
    marginTop: 8,
  },

  exploreButton: {
    alignSelf: "flex-start",
    backgroundColor: "#FFFFFF",
    paddingHorizontal: 17,
    paddingVertical: 9,
    borderRadius: 20,
    marginTop: 15,
  },

  exploreText: {
    color: "#A83E20",
    fontSize: 12,
    fontWeight: "800",
  },

  foodEmoji: {
    position: "absolute",
    right: -5,
    bottom: 5,
    fontSize: 100,
  },

  sectionHeader: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    marginTop: 26,
    marginBottom: 13,
  },

  sectionTitle: {
    fontSize: 18,
    fontWeight: "800",
    color: "#542719",
  },

  seeAll: {
    fontSize: 12,
    fontWeight: "700",
    color: "#C84A25",
  },

  categories: {
    gap: 12,
    paddingRight: 10,
  },

  category: {
    width: 78,
    height: 90,
    backgroundColor: "#FFFFFF",
    borderRadius: 18,
    justifyContent: "center",
    alignItems: "center",
    borderWidth: 1,
    borderColor: "#F0E3D7",
  },

  categoryEmoji: {
    fontSize: 31,
    marginBottom: 7,
  },

  categoryTitle: {
    fontSize: 11,
    fontWeight: "700",
    color: "#684638",
  },

  mealCard: {
    backgroundColor: "#FFFFFF",
    borderRadius: 20,
    padding: 12,
    marginBottom: 12,
    flexDirection: "row",
    alignItems: "center",
    borderWidth: 1,
    borderColor: "#F0E3D7",
  },

  mealImage: {
    width: 82,
    height: 82,
    borderRadius: 16,
    backgroundColor: "#FFF0E3",
    justifyContent: "center",
    alignItems: "center",
  },

  mealEmoji: {
    fontSize: 45,
  },

  mealInfo: {
    flex: 1,
    marginLeft: 13,
    paddingRight: 35,
  },

  mealName: {
    fontSize: 15,
    fontWeight: "800",
    color: "#542719",
  },

  mealDescription: {
    fontSize: 11,
    color: "#8A7065",
    marginTop: 5,
    lineHeight: 16,
  },

  mealBottom: {
    flexDirection: "row",
    justifyContent: "space-between",
    marginTop: 8,
  },

  rating: {
    fontSize: 11,
    color: "#B96A18",
    fontWeight: "700",
  },

  price: {
    fontSize: 14,
    color: "#C84A25",
    fontWeight: "800",
  },

  addButton: {
    position: "absolute",
    right: 12,
    bottom: 12,
    width: 30,
    height: 30,
    borderRadius: 15,
    backgroundColor: "#C84A25",
    justifyContent: "center",
    alignItems: "center",
  },

  addButtonText: {
    color: "#FFFFFF",
    fontSize: 20,
  },
});