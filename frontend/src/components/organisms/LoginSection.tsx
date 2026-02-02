"use client";

import React, { useState } from "react";
import Link from "next/link";
import { Logo } from "../atoms/Logo";
import { LoginForm } from "../molecules/LoginForm";
import { MfaForm } from "../molecules/MfaForm";
import { useLogin } from "@/hooks/useLogin";
import { useMfaVerify } from "@/hooks/useMfaVerify";

export const LoginSection = () => {
  const [mfaRequired, setMfaRequired] = useState(false);
  const [tempToken, setTempToken] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);
  const { login, loading: loginLoading, error: loginError } = useLogin();
  const { verify, loading: mfaLoading, error: mfaError } = useMfaVerify();

  const handleLoginSubmit = async (data: { phone: string; password: string }) => {
    setSuccessMessage(null);
    const result = await login({ phone: data.phone, password: data.password });
    if (!result.ok) return;

    if (result.mfaRequired) {
      setTempToken(result.tempToken);
      setMfaRequired(true);
      return;
    }

    localStorage.setItem("accessToken", result.accessToken);
    setMfaRequired(false);
    setTempToken(null);
    setSuccessMessage("Login exitoso.");
  };

  const handleMfaSubmit = (otp: string) => {
    if (!tempToken) return;
    setSuccessMessage(null);
    verify({ tempToken, code: otp }).then((result) => {
      if (!result.ok) return;
      localStorage.setItem("accessToken", result.accessToken);
      setMfaRequired(false);
      setTempToken(null);
      setSuccessMessage("Login exitoso.");
    });
  };

  return (
    <section className="min-h-[100dvh] flex items-center justify-center bg-bg-dark px-4 py-12">
      <div className="w-full max-w-md flex flex-col items-center gap-8">
        <Logo />
        <div className="text-center space-y-2">
          <h1 className="text-3xl font-bold text-text-light uppercase tracking-wider">
            {mfaRequired ? "Verificación" : "Iniciar Sesión"}
          </h1>
          <p className="text-text-light/50">
            {mfaRequired
              ? "Introduce el código enviado a tu móvil"
              : "Accede a tu cuenta segura"}
          </p>
        </div>

        <div className="w-full bg-bg-dark p-8 rounded-3xl border-2 border-primary/10">
          {!mfaRequired ? (
            <LoginForm
              onSubmit={handleLoginSubmit}
              loading={loginLoading}
              error={loginError}
            />
          ) : (
            <MfaForm
              onSubmit={handleMfaSubmit}
              loading={mfaLoading}
              error={mfaError}
            />
          )}
        </div>

        {!mfaRequired && (
          <p className="text-text-light/60 text-sm">
            ¿No tienes cuenta?{" "}
            <Link
              href="/register"
              className="text-primary font-medium hover:underline"
            >
              Regístrate
            </Link>
          </p>
        )}

        {mfaRequired && (
          <button
            onClick={() => setMfaRequired(false)}
            className="text-primary text-sm font-medium hover:underline"
          >
            Volver al login
          </button>
        )}
        {successMessage && (
          <p className="text-text-light text-sm text-center">{successMessage}</p>
        )}
      </div>
    </section>
  );
};
