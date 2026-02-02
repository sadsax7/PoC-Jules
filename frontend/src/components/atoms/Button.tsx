import Link from "next/link";
import React from "react";

interface ButtonProps {
  variant: "primary" | "outline";
  href: string;
  children: React.ReactNode;
  className?: string;
}

export const Button = ({
  variant,
  href,
  children,
  className = "",
}: ButtonProps) => {
  const baseStyles =
    "px-8 py-3 rounded-full font-semibold transition-all duration-200 inline-block text-center min-w-[160px]";

  const variantStyles = {
    primary: "bg-primary text-text-light hover:opacity-90 active:scale-95",
    outline: "border-2 border-primary text-primary hover:bg-primary/5 active:scale-95",
  };

  return (
    <Link
      href={href}
      className={`${baseStyles} ${variantStyles[variant]} ${className}`}
    >
      {children}
    </Link>
  );
};
