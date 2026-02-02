"use client";

import React, { useState } from "react";
import Link from "next/link";
import { Logo } from "../atoms/Logo";
import { LoginForm } from "../molecules/LoginForm";
import { MfaForm } from "../molecules/MfaForm";

export const LoginSection = () => {
  const [mfaRequired, setMfaRequired] = useState(false);

  const handleLoginSubmit = (data: { phone: string }) => {
    console.log("Login submitted for:", data.phone);
    setMfaRequired(true);
  };

  const handleMfaSubmit = (otp: string) => {
    console.log("MFA verified with code:", otp);
    // Simulating success
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
            <LoginForm onSubmit={handleLoginSubmit} />
          ) : (
            <MfaForm onSubmit={handleMfaSubmit} />
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
      </div>
    </section>
  );
};
