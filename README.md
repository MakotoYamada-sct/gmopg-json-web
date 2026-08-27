# gmopg-json-web

`gmopg-json-web` は、GMOペイメントゲートウェイ（GMO-PG）のJSON API（OpenAPI）を利用した決済処理・Web連携を簡単に構築するためのクライアントライブラリ / Webラッパーパッケージです。

---

## 概要

本ライブラリは、GMO-PGが提供する決済API（クレジットカード決済・マルチ決済など）とのリクエスト/レスポンスの暗号化・認証・JSON通信を抽象化し、Webアプリケーションへの組み込みを容易にします。

---

## 主な機能

- **取引・決済管理**: 取引登録（ExecTran / EntryTran）、決済実行、キャンセル・金額変更
- **会員・カード管理**: 会員IDの作成・更新、クレジットカード情報の保存・削除・参照
- **3Dセキュア対応**: 3Dセキュア 2.0 認証フローおよびレスポンスのハンドリング
- **セキュリティ**: APIキー認証や署名生成処理の自動化

---

## インストール

プロジェクトの依存関係に合わせて以下のコマンドでインストールしてください。

```bash
# Node.js / npm の場合
npm install [https://github.com/MakotoYamada-sct/gmopg-json-web.git](https://github.com/MakotoYamada-sct/gmopg-json-web.git)

# または yarn の場合
yarn add [https://github.com/MakotoYamada-sct/gmopg-json-web.git](https://github.com/MakotoYamada-sct/gmopg-json-web.git)
