export default function StyleguidePage() {
  return (
    <main className="min-h-screen bg-gradient-to-br from-white via-white to-orange-50 px-6 py-12 text-black">
      <section className="mx-auto flex w-full max-w-5xl flex-col gap-6">
        <div className="rounded-3xl border border-black/10 bg-white p-8 shadow-[0_20px_60px_-30px_rgba(0,0,0,0.3)]">
          <p className="text-sm uppercase tracking-[0.25em] text-black/60">
            PoC Wallet · Styleguide
          </p>
          <h1 className="mt-3 text-4xl font-semibold leading-tight sm:text-5xl">
            Sistema visual institucional
          </h1>
          <p className="mt-4 max-w-2xl text-base text-black/70">
            Tipografia Inter, paleta naranja/negro/blanco y reglas de contraste
            para un UI consistente desde el primer componente.
          </p>
        </div>

        <div className="grid gap-6 md:grid-cols-2">
          <div className="rounded-3xl border border-black/10 bg-white p-6">
            <h2 className="text-xl font-semibold">Colores</h2>
            <div className="mt-4 grid gap-4">
              <div className="flex items-center justify-between rounded-2xl border border-black/10 bg-primary px-4 py-3 text-text-light">
                <span className="font-medium">Primary</span>
                <span className="text-sm">#FF6B00</span>
              </div>
              <div className="flex items-center justify-between rounded-2xl border border-black/10 bg-bg-dark px-4 py-3 text-text-light">
                <span className="font-medium">Background Dark</span>
                <span className="text-sm">#000000</span>
              </div>
              <div className="flex items-center justify-between rounded-2xl border border-black/10 bg-white px-4 py-3 text-black">
                <span className="font-medium">Text Light</span>
                <span className="text-sm">#FFFFFF</span>
              </div>
            </div>
          </div>

          <div className="rounded-3xl border border-black/10 bg-white p-6">
            <h2 className="text-xl font-semibold">Tipografia</h2>
            <div className="mt-4 space-y-3">
              <p className="text-3xl font-semibold">Inter Semibold 32</p>
              <p className="text-lg">Inter Regular 18</p>
              <p className="text-sm uppercase tracking-[0.2em] text-black/60">
                Inter Uppercase 12
              </p>
            </div>
          </div>
        </div>

        <div className="rounded-3xl border border-black/10 bg-white p-6">
          <h2 className="text-xl font-semibold">Estados y botones</h2>
          <div className="mt-4 flex flex-wrap gap-4">
            <button className="rounded-full bg-primary px-5 py-2 text-sm font-semibold text-text-light shadow-[0_10px_25px_-12px_rgba(255,107,0,0.8)]">
              Accion primaria
            </button>
            <button className="rounded-full border border-primary px-5 py-2 text-sm font-semibold text-primary">
              Accion secundaria
            </button>
            <button className="rounded-full border border-black/15 px-5 py-2 text-sm text-black/70">
              Neutro
            </button>
          </div>
        </div>
      </section>
    </main>
  );
}
