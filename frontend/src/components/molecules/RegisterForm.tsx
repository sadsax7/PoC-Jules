"use client";

import React, { useState } from "react";
import { Input } from "../atoms/Input";
import { Button } from "../atoms/Button";

export const RegisterForm = () => {
  const [formData, setFormData] = useState({
    phone: "",
    email: "",
    password: "",
  });

  const [touched, setTouched] = useState({
    phone: false,
    email: false,
    password: false,
  });

  const validateEmail = (email: string) => {
    if (!email) return true;
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(email);
  };

  // Derive errors and validity during render
  const errors = {
    phone: formData.phone.trim() === "" ? "El teléfono es obligatorio" : "",
    password:
      formData.password.trim() === "" ? "La contraseña es obligatoria" : "",
    email:
      formData.email && !validateEmail(formData.email)
        ? "Email inválido"
        : "",
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
    if (isFormValid) {
      console.log("Form submitted:", formData);
    }
  };

  return (
    <form
      onSubmit={handleSubmit}
      className="flex flex-col gap-6 w-full max-w-md"
    >
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
        label="Email (opcional)"
        name="email"
        type="email"
        placeholder="ejemplo@correo.com"
        value={formData.email}
        onChange={handleChange}
        onBlur={handleBlur}
        error={touched.email ? errors.email : ""}
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
        disabled={!isFormValid}
        className="mt-4"
      >
        Registrarse
      </Button>
    </form>
  );
};
