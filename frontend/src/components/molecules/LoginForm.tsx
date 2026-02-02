"use client";

import React, { useState } from "react";
import { Input } from "../atoms/Input";
import { Button } from "../atoms/Button";

interface LoginFormProps {
  onSubmit: (data: { phone: string; password: string }) => void;
  loading?: boolean;
  error?: string | null;
}

export const LoginForm = ({ onSubmit, loading = false, error }: LoginFormProps) => {
  const [formData, setFormData] = useState({
    phone: "",
    password: "",
  });

  const [touched, setTouched] = useState({
    phone: false,
    password: false,
  });

  // Derive errors and validity during render
  const errors = {
    phone: formData.phone.trim() === "" ? "El teléfono es obligatorio" : "",
    password:
      formData.password.trim() === "" ? "La contraseña es obligatoria" : "",
  };

  const isFormValid = !Object.values(errors).some((error) => error !== "");

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleBlur = (e: React.FocusEvent<HTMLInputElement>) => {
    const { name } = e.target;
    setTouched((prev) => ({ ...prev, [name]: true }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!isFormValid) {
      setTouched({ phone: true, password: true });
      return;
    }
    onSubmit({ phone: formData.phone.trim(), password: formData.password });
  };

  return (
    <form onSubmit={handleSubmit} className="flex flex-col gap-6 w-full">
      <Input
        label="Teléfono"
        name="phone"
        type="tel"
        placeholder="+34 600 000 000"
        value={formData.phone}
        onChange={handleChange}
        onBlur={handleBlur}
        error={touched.phone ? errors.phone : ""}
        required
      />
      <Input
        label="Contraseña"
        name="password"
        type="password"
        placeholder="••••••••"
        value={formData.password}
        onChange={handleChange}
        onBlur={handleBlur}
        error={touched.password ? errors.password : ""}
        required
      />
      <Button
        variant="primary"
        type="submit"
        disabled={!isFormValid || loading}
        className="mt-4 w-full"
      >
        {loading ? "Ingresando..." : "Ingresar"}
      </Button>
      {error && (
        <p className="text-primary text-sm font-medium text-center">{error}</p>
      )}
    </form>
  );
};
