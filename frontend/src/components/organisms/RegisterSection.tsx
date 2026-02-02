import React from "react";
import Link from "next/link";
import { RegisterForm } from "../molecules/RegisterForm";
import { Logo } from "../atoms/Logo";

export const RegisterSection = () => {
  return (
    <section className="min-h-[100dvh] flex items-center justify-center bg-bg-dark px-4 py-12">
      <div className="w-full max-w-md flex flex-col items-center gap-8">
        <Logo />
        <div className="text-center space-y-2">
          <h1 className="text-3xl font-bold text-text-light uppercase tracking-wider">
            Registro
          </h1>
          <p className="text-text-light/50">Crea tu cuenta en segundos</p>
        </div>
        <div className="w-full bg-bg-dark p-8 rounded-3xl border-2 border-primary/10">
          <RegisterForm />
        </div>
        <p className="text-text-light/60 text-sm">
          ¿Ya tienes cuenta?{" "}
          <Link
            href="/login"
            className="text-primary font-medium hover:underline"
          >
            Inicia sesión
          </Link>
        </p>
      </div>
    </section>
  );
};
