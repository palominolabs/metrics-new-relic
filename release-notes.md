# 1.1.0, 1.1.1

- Switch to bintray for artifact distribution. Use [jcenter](https://bintray.com/bintray/jcenter) to get artifacts
  - For Gradle, use `jcenter()` in your repositories block.
  - For Maven, click the "Set me up" button in the above link.
  - 1.1.0 was tagged but no artifacts were shipped during the changeover to bintray.
- Switch to builder for `NewRelicReporter`
- Add `TableMetricAttributeFilter` and associated yaml loader

# 1.0.5

- Update metrics and new relic dependencies

# 1.0.4

- Update new relic dependency

# 1.0.3

- Remove stray println

# 1.0.2 

- Nothing: accidental release

# 1.0.1

- Compatible with Java 6

# 1.0.0

- Initial release
