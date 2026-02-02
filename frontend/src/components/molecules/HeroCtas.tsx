import { Button } from "../atoms/Button";

export const HeroCtas = () => {
  return (
    <div className="flex flex-col sm:flex-row gap-4 w-full justify-center sm:justify-start">
      <Button variant="outline" href="/login">
        Ingresar
      </Button>
      <Button variant="primary" href="/register">
        Registrarse
      </Button>
    </div>
  );
};
