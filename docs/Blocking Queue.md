# Blocking Queue

Ref: [MPA-24](https://github.com/Nitesh-Nandan/master-system-java/pull/2)

- Many Implementation call notifyall() before removing or adding a record in get and add method respectively.
- Plan to write a scenario that mimic this edge case and justify the current solution correctness.
