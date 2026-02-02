"use client";

import React, { useRef } from "react";

interface OtpInputProps {
  value: string;
  onChange: (value: string) => void;
  error?: string;
}

export const OtpInput = ({ value, onChange, error }: OtpInputProps) => {
  const inputsRef = useRef<(HTMLInputElement | null)[]>([]);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement>,
    index: number
  ) => {
    const val = e.target.value;
    // Only allow digits
    if (!/^\d*$/.test(val)) return;

    // Take only the last character if multiple are entered (e.g. via some mobile keyboards)
    const singleDigit = val.slice(-1);

    const newValue = value.split("");
    // Ensure the array has 6 elements
    while (newValue.length < 6) newValue.push("");

    newValue[index] = singleDigit;
    const combinedValue = newValue.join("").slice(0, 6);
    onChange(combinedValue);

    // Move to next input if digit was entered
    if (singleDigit && index < 5) {
      inputsRef.current[index + 1]?.focus();
    }
  };

  const handleKeyDown = (
    e: React.KeyboardEvent<HTMLInputElement>,
    index: number
  ) => {
    if (e.key === "Backspace" && !value[index] && index > 0) {
      inputsRef.current[index - 1]?.focus();
    }
  };

  const handlePaste = (e: React.ClipboardEvent) => {
    e.preventDefault();
    const pasteData = e.clipboardData.getData("text").trim().slice(0, 6);
    if (/^\d+$/.test(pasteData)) {
      onChange(pasteData);
      // Focus the next empty input or the last one
      const nextIndex = Math.min(pasteData.length, 5);
      inputsRef.current[nextIndex]?.focus();
    }
  };

  return (
    <div className="flex flex-col gap-2 w-full">
      <div className="flex justify-between gap-2" onPaste={handlePaste}>
        {[0, 1, 2, 3, 4, 5].map((index) => (
          <input
            key={index}
            ref={(el) => {
              inputsRef.current[index] = el;
            }}
            type="text"
            inputMode="numeric"
            autoComplete="one-time-code"
            value={value[index] || ""}
            onChange={(e) => handleChange(e, index)}
            onKeyDown={(e) => handleKeyDown(e, index)}
            className={`
              w-full h-14 text-center text-xl font-bold rounded-xl bg-bg-dark border-2
              text-text-light outline-none transition-all duration-200
              ${
                error
                  ? "border-primary"
                  : "border-text-light/10 focus:border-primary"
              }
            `}
            aria-label={`Código dígito ${index + 1}`}
          />
        ))}
      </div>
      {error && (
        <span className="text-primary text-xs mt-1 ml-1 font-medium text-center">
          {error}
        </span>
      )}
    </div>
  );
};
