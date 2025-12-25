import { defineConfig, globalIgnores } from 'eslint/config'
import globals from 'globals'
import js from '@eslint/js'
import pluginVue from 'eslint-plugin-vue'
import skipFormatting from '@vue/eslint-config-prettier/skip-formatting'

export default defineConfig([
  {
    name: 'app/files-to-lint',
    files: ['**/*.{js,mjs,jsx,vue}'],
  },

  globalIgnores(['**/dist/**', '**/dist-ssr/**', '**/coverage/**']),

  {
    languageOptions: {
      globals: {
        ...globals.browser,
      },
    },
  },

  js.configs.recommended,
  ...pluginVue.configs['flat/essential'],
  ...pluginVue.configs['flat/strongly-recommended'],
  ...pluginVue.configs['flat/recommended'],
  skipFormatting,

  {
    rules: {
      'vue/multi-word-component-names': 'off',
      'vue/one-component-per-file': 'warn',
      'vue/html-self-closing': [
        'error',
        {
          html: {
            void: 'always',
            normal: 'never',
            component: 'always',
          },
        },
      ],
      'vue/component-definition-name-casing': ['error', 'PascalCase'],
      'vue/require-default-prop': 'off',
      'vue/require-explicit-emits': 'warn',

      semi: ['error', 'never'],
      quotes: ['error', 'single'],
      'quote-props': ['error', 'as-needed'],
      'arrow-parens': ['error', 'always'],
      'comma-dangle': ['error', 'always-multiline'],
      'object-curly-spacing': ['error', 'always'],
      'array-bracket-spacing': ['error', 'never'],

      'no-unused-vars': 'warn',
      'no-undef': 'error',
    },
  },
])
