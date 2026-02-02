import type { NextConfig } from "next";

/** Base Next.js config for the PoC Wallet frontend. */
const nextConfig: NextConfig = {
  async rewrites() {
    return [
      {
        source: "/api/:path*",
        destination: "http://localhost:8080/:path*",
      },
    ];
  },
};

export default nextConfig;
