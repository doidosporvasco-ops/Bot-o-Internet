# Botão Internet (Android 11+)

Aplicativo Android nativo em Java, mínimo Android 11 (API 30), sem bibliotecas externas.

## O que funciona
- Bolha flutuante arrastável sobre outros aplicativos após autorização.
- Toque abre o painel de conectividade do sistema Android, ou configurações de rede como alternativa.
- Serviço em primeiro plano com notificação persistente.
- Botões para iniciar e parar.
- Sem permissão de internet, coleta de dados, root ou serviço de acessibilidade.

## Limitações honestas
- **Não liga/desliga os dados móveis automaticamente.** O Android não disponibiliza API pública para aplicativos comuns realizarem isso.
- **Não mostra verde/vermelho indicando o estado real.** A bolha é azul, para evitar informações enganosas.
- Fabricantes podem limitar sobreposições e funcionamento em segundo plano; teste em aparelhos reais.
- A permissão de notificação é solicitada no Android 13+; alguns fabricantes podem pedir outras configurações de bateria.

## Compilar online no GitHub
1. Crie um repositório vazio (de preferência privado).
2. Envie o **conteúdo** desta pasta para a raiz do repositório, incluindo `.github/workflows/android.yml`.
3. Vá em **Actions** > **Gerar APK Botao Internet** > **Run workflow** (ou aguarde o push em main).
4. Após concluir, abra a execução e baixe o artefato **BotaoInternet-APK**.
5. Extraia o ZIP do artefato e instale `app-debug.apk` no celular (autorize instalação de fonte externa quando solicitado).

## Distribuição
O APK de depuração é assinado automaticamente para testes e distribuição privada inicial. Para atualizações consistentes entre amigos, gere e guarde uma chave de assinatura de release; não compartilhe a chave. Não envie APKs modificados por terceiros.

## Segurança
Não é necessário habilitar acessibilidade, root, ADB, ou fornecer dados pessoais. O usuário controla manualmente a alteração no painel do Android.
