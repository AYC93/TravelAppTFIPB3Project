name: Continuous Build and Deploy Railway

on:
  push:
    branches:
      - main
    paths:
      - 'server/**'

jobs:
  deploy:
    runs-on: ubuntu-latest

    steps:
      - name: checkout
        uses: actions/checkout@v3

      - name: setup jdk 
        uses: actions/setup-java@v3
        with:
          distribution: 'temurin'
          java-version: '17'

      - name: mvn build
        run: cd server && mvn package -DskipTests
                
      - name: Install railway
        run: |
          curl -fsSL cli.new | bash

      - name: Deploy to railway
        env: 
          RAILWAY_TOKEN: ${{ secrets.RAILWAY_TOKEN }}
          RAILWAY_PROJECT: 64ebdb7f-b3c0-4849-9cbb-8aad432d1a22
        run: |
            cd server
            RAILWAY_TOKEN=$RAILWAY_TOKEN railway up

https://github.com/railwayapp/cli/issues/105 no resolution to this
        
