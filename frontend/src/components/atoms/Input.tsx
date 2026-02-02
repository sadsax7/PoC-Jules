import React from "react";

interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label: string;
  error?: string;
}

export const Input = ({
  label,
  error,
  className = "",
  id,
  ...props
}: InputProps) => {
  const inputId = id || label.toLowerCase().replace(/\s/g, "-");

  return (
    <div className="flex flex-col gap-1 w-full text-left">
      <label
        htmlFor={inputId}
        className="text-text-light font-medium text-sm ml-1 cursor-pointer"
      >
        {label}
      </label>
      <input
        id={inputId}
        className={`
          w-full px-4 py-3 rounded-xl bg-bg-dark border-2
          text-text-light outline-none transition-all duration-200
          placeholder:text-text-light/30
          ${
            error
              ? "border-primary"
              : "border-text-light/10 focus:border-primary"
          }
          ${className}
        `}
        {...props}
      />
      {error && (
        <span className="text-primary text-xs mt-1 ml-1 font-medium">
          {error}
        </span>
      )}
    </div>
  );
};
