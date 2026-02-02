"use client";

import React, { useState } from "react";
import { OtpInput } from "../atoms/OtpInput";
import { Button } from "../atoms/Button";

interface MfaFormProps {
  onSubmit: (otp: string) => void;
  loading?: boolean;
  error?: string | null;
}

export const MfaForm = ({ onSubmit, loading = false, error }: MfaFormProps) => {
  const [otp, setOtp] = useState("");
  const [localError, setLocalError] = useState("");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (otp.length !== 6) {
      setLocalError("Ingresa el código de 6 dígitos");
      return;
    }
    setLocalError("");
    onSubmit(otp);
  };

  return (
    <form onSubmit={handleSubmit} className="flex flex-col gap-8 w-full">
      <div className="space-y-4">
        <p className="text-text-light/80 text-center text-sm">
          Ingresa tu código de 6 dígitos
        </p>
        <OtpInput value={otp} onChange={setOtp} error={localError || error || undefined} />
        <p className="text-text-light/40 text-center text-xs">
          Código de prueba: <span className="text-primary/60">123456</span>
        </p>
      </div>
      <Button
        variant="primary"
        type="submit"
        disabled={otp.length !== 6 || loading}
        className="w-full"
      >
        {loading ? "Verificando..." : "Verificar"}
      </Button>
      {error && (
        <p className="text-primary text-sm font-medium text-center">{error}</p>
      )}
    </form>
  );
};
