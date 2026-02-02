import { Logo } from "../atoms/Logo";
import { HeroCtas } from "../molecules/HeroCtas";

export const HeroSection = () => {
  return (
    <section className="flex flex-col items-center text-center py-20 px-6 max-w-5xl mx-auto w-full">
      <div className="mb-6">
        <Logo />
      </div>
      <h1 className="text-5xl md:text-7xl font-extrabold text-bg-dark mb-6 tracking-tight">
        Tu Wallet, <br /> simplificada.
      </h1>
      <p className="text-lg md:text-xl text-bg-dark/70 mb-10 max-w-xl">
        La plataforma más segura y fácil de usar para gestionar tus activos
        digitales. Comienza hoy mismo y toma el control de tus finanzas.
      </p>
      <HeroCtas />
    </section>
  );
};
