import { useCallback, useState } from "react";

type RegisterPayload = {
  phone: string;
  email?: string;
  password: string;
};

type RegisterResult = {
  userId?: string;
};

type RegisterState = {
  loading: boolean;
  success: boolean;
  error: string | null;
  errorCode: string | null;
};

export const useRegister = () => {
  const [state, setState] = useState<RegisterState>({
    loading: false,
    success: false,
    error: null,
    errorCode: null,
  });

  const register = useCallback(async (payload: RegisterPayload) => {
    setState({ loading: true, success: false, error: null, errorCode: null });

    const baseUrl = process.env.NEXT_PUBLIC_API_BASE_URL ?? "/api";
    const requestBody = {
      phone: payload.phone,
      password: payload.password,
      ...(payload.email ? { email: payload.email } : {}),
    };

    try {
      const response = await fetch(`${baseUrl}/auth/register`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(requestBody),
      });

      let data: RegisterResult & { errorCode?: string; message?: string } | null = null;
      try {
        data = await response.json();
      } catch {
        data = null;
      }

      if (!response.ok) {
        const errorCode = data?.errorCode ?? null;
        const message =
          response.status === 409 || errorCode === "USER_ALREADY_EXISTS"
            ? "Teléfono ya registrado"
            : data?.message ?? "Error al registrar";

        setState({
          loading: false,
          success: false,
          error: message,
          errorCode,
        });
        return { ok: false as const };
      }

      setState({ loading: false, success: true, error: null, errorCode: null });
      return { ok: true as const, data: data ?? {} };
    } catch {
      setState({
        loading: false,
        success: false,
        error: "Error de red",
        errorCode: null,
      });
      return { ok: false as const };
    }
  }, []);

  return {
    ...state,
    register,
  };
};
