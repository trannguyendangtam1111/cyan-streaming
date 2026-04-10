/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,jsx}"],
  theme: {
    extend: {
      colors: {
        cyanBrand: "#06b6d4",
        midnight: "#0f172a",
      },
      fontFamily: {
        display: ["Bebas Neue", "sans-serif"],
        body: ["Space Grotesk", "sans-serif"],
      },
      boxShadow: {
        cyan: "0 0 40px rgba(6, 182, 212, 0.25)",
      },
    },
  },
  plugins: [],
};
