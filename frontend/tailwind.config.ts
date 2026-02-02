import type { Config } from "tailwindcss";

/** Tailwind theme tokens for the PoC Wallet design system. */
const config: Config = {
  content: ["./src/**/*.{ts,tsx}"],
  darkMode: "class",
  theme: {
    extend: {
      colors: {
        primary: "#FF6B00",
        "bg-dark": "#000000",
        "text-light": "#FFFFFF",
      },
    },
  },
  plugins: [],
};

export default config;
