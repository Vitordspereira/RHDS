# RHDS • Correções finais aplicadas (backend)

## Segurança / autenticação
- cadeia stateless com `JwtAuthenticationFilter`
- CORS aberto para ambientes publicados
- extração do principal via JWT padronizada

## Candidato
- aliases `/candidatos` e `/candidato`
- `/candidato/me` e `/candidatos/me` funcionando com usuário autenticado
- update parcial preservando dados existentes
- DTO de perfil retornando e-mail

## Empresa / recrutador
- `/empresa/me`, `/recrutador/me` e aliases consolidados
- atualização de empresa pelo usuário autenticado

## Vagas
- `/vagas/me` agora lista vagas da empresa do recrutador autenticado
- `PUT /vagas/{id}`, `DELETE /vagas/{id}` e `POST /vagas/{id}/encerrar` com ownership por empresa/recrutador
- edição não recria processo seletivo duplicado
- serialização de listas de vaga padronizada via mapper JSON

## Processo seletivo
- criação idempotente: se o processo da vaga já existir, não duplica

## Candidaturas
- `POST /candidaturas` aceita payload com `vagaId`/`idVaga`
- quando `candidatoId` não vier no corpo, usa o candidato autenticado pelo e-mail do JWT

## Alertas
- criação retorna mensagem JSON
- suporte a cancelamento por token em `/alerta/cancelar?token=...`

## Tratamento de erros
- handlers globais mais claros para 400/404/409/validação
- removido comportamento de mascarar falhas de update como 404 genérico

## Observação de teste
- o ambiente não possui Maven funcional para build completo do Spring Boot
- as correções foram revisadas estaticamente e alinhadas com o front e o dump do banco
