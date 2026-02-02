import { useCallback, useState } from "react";

type LoginPayload = {
  phone: string;
  password: string;
};

type LoginResponse = {
  accessToken?: string;
  status?: string;
  tempToken?: string;
  errorCode?: string;
  message?: string;
};

type LoginState = {
  loading: boolean;
  error: string | null;
  errorCode: string | null;
};

type LoginResult =
  | { ok: true; accessToken: string; mfaRequired: false }
  | { ok: true; tempToken: string; mfaRequired: true }
  | { ok: false };

export const useLogin = () => {
  const [state, setState] = useState<LoginState>({
    loading: false,
    error: null,
    errorCode: null,
  });

  const login = useCallback(async (payload: LoginPayload): Promise<LoginResult> => {
    setState({ loading: true, error: null, errorCode: null });

    const baseUrl = process.env.NEXT_PUBLIC_API_BASE_URL ?? "/api";

    try {
      const response = await fetch(`${baseUrl}/auth/login`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          phone: payload.phone,
          password: payload.password,
        }),
      });

      let data: LoginResponse | null = null;
      try {
        data = await response.json();
      } catch {
        data = null;
      }

      if (!response.ok) {
        const errorCode = data?.errorCode ?? null;
        const message =
          errorCode === "INVALID_CREDENTIALS"
            ? "Credenciales inválidas"
            : data?.message ?? "Error al iniciar sesión";

        setState({ loading: false, error: message, errorCode });
        return { ok: false };
      }

      if (data?.status === "MFA_REQUIRED" && data.tempToken) {
        setState({ loading: false, error: null, errorCode: null });
        return { ok: true, mfaRequired: true, tempToken: data.tempToken };
      }

      if (data?.accessToken) {
        setState({ loading: false, error: null, errorCode: null });
        return { ok: true, mfaRequired: false, accessToken: data.accessToken };
      }

      setState({
        loading: false,
        error: "Respuesta inesperada del servidor",
        errorCode: null,
      });
      return { ok: false };
    } catch {
      setState({ loading: false, error: "Error de red", errorCode: null });
      return { ok: false };
    }
  }, []);

  return {
    ...state,
    login,
  };
};
