import { useCallback, useState } from "react";

type MfaPayload = {
  tempToken: string;
  code: string;
};

type MfaResponse = {
  accessToken?: string;
  errorCode?: string;
  message?: string;
};

type MfaState = {
  loading: boolean;
  error: string | null;
  errorCode: string | null;
};

type MfaResult =
  | { ok: true; accessToken: string }
  | { ok: false };

export const useMfaVerify = () => {
  const [state, setState] = useState<MfaState>({
    loading: false,
    error: null,
    errorCode: null,
  });

  const verify = useCallback(async (payload: MfaPayload): Promise<MfaResult> => {
    setState({ loading: true, error: null, errorCode: null });

    const baseUrl = process.env.NEXT_PUBLIC_API_BASE_URL ?? "/api";

    try {
      const response = await fetch(`${baseUrl}/auth/mfa/verify`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          tempToken: payload.tempToken,
          code: payload.code,
        }),
      });

      let data: MfaResponse | null = null;
      try {
        data = await response.json();
      } catch {
        data = null;
      }

      if (!response.ok) {
        const errorCode = data?.errorCode ?? null;
        const message =
          errorCode === "MFA_INVALID_CODE"
            ? "Código inválido"
            : errorCode === "INVALID_TOKEN"
              ? "Token inválido o expirado"
              : data?.message ?? "Error al verificar";

        setState({ loading: false, error: message, errorCode });
        return { ok: false };
      }

      if (data?.accessToken) {
        setState({ loading: false, error: null, errorCode: null });
        return { ok: true, accessToken: data.accessToken };
      }

      setState({ loading: false, error: "Respuesta inesperada", errorCode: null });
      return { ok: false };
    } catch {
      setState({ loading: false, error: "Error de red", errorCode: null });
      return { ok: false };
    }
  }, []);

  return {
    ...state,
    verify,
  };
};
