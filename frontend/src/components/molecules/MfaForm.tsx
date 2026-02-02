"use client";

import React, { useState } from "react";
import { OtpInput } from "../atoms/OtpInput";
import { Button } from "../atoms/Button";

interface MfaFormProps {
  onSubmit: (otp: string) => void;
}

export const MfaForm = ({ onSubmit }: MfaFormProps) => {
  const [otp, setOtp] = useState("");
  const [error, setError] = useState("");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (otp.length !== 6) {
      setError("Ingresa el código de 6 dígitos");
      return;
    }
    setError("");
    onSubmit(otp);
  };

  return (
    <form onSubmit={handleSubmit} className="flex flex-col gap-8 w-full">
      <div className="space-y-4">
        <p className="text-text-light/80 text-center text-sm">
          Ingresa tu código de 6 dígitos
        </p>
        <OtpInput value={otp} onChange={setOtp} error={error} />
        <p className="text-text-light/40 text-center text-xs">
          Código de prueba: <span className="text-primary/60">123456</span>
        </p>
      </div>
      <Button
        variant="primary"
        type="submit"
        disabled={otp.length !== 6}
        className="w-full"
      >
        Verificar
      </Button>
    </form>
  );
};
