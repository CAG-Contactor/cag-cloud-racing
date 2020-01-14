const {writeFile} = require('fs')

// Would be passed to script like this:
// `ts-node set-env.ts --environment=dev`
// we get it from yargs's argv object
const API_ENDPOINT = process.env.API_ENDPOINT || 'http://localhost:3000'
const WS_ENDPOINT = process.env.WS_ENDPOINT || 'ws://localhost:3000'

const targetPath = `./proxy.local-env.conf.json`
const envConfigFile = `{
 "/api": {
    "target": "${API_ENDPOINT}",
    "changeOrigin": true,
    "pathRewrite": {
      "^/api": ""
    }
  },
  "/ws": {
    "target": "${WS_ENDPOINT}",
    "ws": true,
    "changeOrigin": true,
    "pathRewrite": {
      "^/ws": ""
    }
  }
}
`
writeFile(targetPath, envConfigFile, function (err) {
  if (err) {
    console.log(err)
  }

  console.log(`Output generated at ${targetPath}`)
})
