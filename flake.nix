{
  description = "A very basic flake";

  inputs.nixpkgs.url = "github:nixos/nixpkgs/nixos-24.05";

  outputs = { self, nixpkgs }: {

    devShells.x86_64-linux.default = let
        pkgs = import nixpkgs { system = "x86_64-linux"; };
        # choose our preferred jdk package
        jdk = pkgs.jdk21;
    in pkgs.mkShell {
        buildInputs = with pkgs; [
          jdk
          scala_3
          # customise the jdk which gradle uses by default
          (callPackage gradle-packages.gradle_8 {
            java = jdk;
          })
        ];
    };
  };
}
