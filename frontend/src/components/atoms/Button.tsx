import Link from "next/link";
import React from "react";

interface ButtonProps {
  variant: "primary" | "outline";
  href?: string;
  type?: "button" | "submit" | "reset";
  disabled?: boolean;
  onClick?: () => void;
  children: React.ReactNode;
  className?: string;
}

export const Button = ({
  variant,
  href,
  type = "button",
  disabled,
  onClick,
  children,
  className = "",
}: ButtonProps) => {
  const baseStyles =
    "px-8 py-3 rounded-full font-semibold transition-all duration-200 inline-block text-center min-w-[160px] cursor-pointer disabled:opacity-50 disabled:cursor-not-allowed";

  const variantStyles = {
    primary: "bg-primary text-text-light hover:opacity-90 active:scale-95",
    outline:
      "border-2 border-primary text-primary hover:bg-primary/5 active:scale-95",
  };

  const combinedClassName = `${baseStyles} ${variantStyles[variant]} ${className}`;

  if (href) {
    return (
      <Link href={href} className={combinedClassName}>
        {children}
      </Link>
    );
  }

  return (
    <button
      type={type}
      disabled={disabled}
      onClick={onClick}
      className={combinedClassName}
    >
      {children}
    </button>
  );
};
