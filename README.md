### Keystore Shared Preferences

A Shared Preferences implementation that encrypts your values with the Keystore mechanism, so you don't need to specify a password, as each user will have unique one generated for them and safely stored in the operating system.

##### Important:

1. This library requires API 18 (Android 4.3) or greater to run.
2. If you allow backup of shared preferences they are very likely to break when the user tries to use them on another device.

##### Example of use:

1. Create Keystore Shared Preferences:
```java
// Your context
final Context context = ...;

// Create your usual Shared Preferences
final SharedPreferences plaintextSharedPreferences = context.getSharedPreferences("shared_preferences_name", Context.MODE_PRIVATE);

// Keystore Shared Preferences will wrap around your usual Shared Preferences and encrypt values that pass through
final SharedPreferences keystoreSharedPreferences = new KeystoreSharedPreferences(context, plaintextSharedPreferences);
```

2. Use Keystore Shared Preferences as you normally would use Shared Preferences:
```java
sharedPreferences
    .edit()
    .putString("string_key", "string_value")
    .commit();
```

3. Is you inspect your Shared Preferences file it will look something like this:
```xml
<?xml version='1.0' encoding='utf-8' standalone='yes' ?>
<map>
    <string name="float_key">DmXBSjLJlm/OgQMsdxL1vT3KrMB/xfllpayLBfrr03FvQpcE09UyMVjPjTP8MKfbwfLb/WKm+y4u&#10;kAND5oozr7nlwqF99NjfuWUQsRHclYkRviBaiMhHvHTlj5tt/NKzmakiQpCry11q5CI/6rQZlGIE&#10;RxXT4IGcC8FIyyy1CgFvGhya+dXMRtBWd/x3bMtl5CLBJ0adiolS6x67O3IxbRK36LkOFQBat4tW&#10;EGgF95is0Q7PiJ/lX/VVelXVag9ipcZgmrBA423HyG7vP269eCh+2VqfYEXa2SR79/cl4W/YkrjP&#10;hoUjvtTt1RWUJIwOiwrHQCYj4JYwAweKb1S+CA==&#10;    </string>
    <string name="string_key">Ri1J/JVA/6BW16+2UOYGOSHMK0W6PGvPi+KqsNBlnPl5ZYWezv68iAGoG+LTj2vJmBhH0ITv5VmU&#10;ygpxirIjWs6QTmQdnAGYBSrsTUXRjALWHM4tliPPRB5eA3Ys+e/IUIBVPEhPbbrPw2nQfZ/VQiBu&#10;hFU6BWfilKji2K9JFIjKTmXNVjL/RqfToho490aVDijMv5wnq+T1K0yMOX2S8XL6dn8HXB6oebto&#10;5B0YRY99Eu+2kZZOD18O1NXEaf9Fnfo+981U9eRJQq+yRLY/PMSCwHzjIGqtXEDF3/nuebd8KwTu&#10;aW6IjcFLPlTaFvPv7vyYkqX4MwHd+XRNB1bQhw==&#10;    </string>
    <set name="string_set_key">
        <string>O803abR5MJWf1Wu6JtZsEmpxF4UNObn+hfJtfOF5Myml3QBlSKG3xSUG4j104CzjjPw7lYV0LplL&#10;Wj0la1IMg9PVGlbffQ6az305V7I5qE09Qq5tjrQF8KifsuZU66s9pRLtwpK4x08nGmpCL3oEExpg&#10;LhPmLUaH6lbp6znPmt+vVi+mVqTUTLReCjJUFxi1GV38c5ljQE9+j71BqXZ+45ywYZt4Ht0YfVSD&#10;Fr/yo0c6W9JHXuvIy9cCIz8nnlwZQKg+93GRXxcI7E5xfwubjFK+pvWJXASM3oucXaZ7zP5p1qSL&#10;RenMZR+sdEMLL1iQdMp5n7gJBQsdksu4ZumFUQ==&#10;        </string>
        <string>R8EGDoG5kVDVyQto+bs3md9LMHalQwrbFICy2iwBunW636QTpnrozkAQoohvhgIcNWfnIumUwrTJ&#10;U+8E53WmzgO6cEVaK4hSP9b4sYk2aEPmdqNwfU+rIYD5kmTxDwGyzino/VH3YOx+tkm/WjlLZp5q&#10;umKK9xQHUv+rcY90Ysf6N14ME39AjrOL3iZVgITMRJ71WcJupAjmGAv0nNqZm5l3gJ6Ustr2bqve&#10;mdov7BoKRNozE1DjpXlBJiQHn89esK/haa5w9bvkuSQgAKjvlgBtDu4eZo46wd9fzDl4+qcgNn7X&#10;0+2NLSvnJ9Gf+oJTGBGVfXkDIeMabgur7AzCvQ==&#10;        </string>
        <string>hX5IxaVbPeUPLC0SOtND5GCH9895GiNK4naCvqpbHUgRwuCdX89lGoyy6hzjzW4ZDhVZwZVe8DDg&#10;exwcn1qmtfqWRnK7N05jjt2gKgVHxmVLp41sGN7AizUicljaQiqrqfzW5AQXEozYZBsTqo/Yifai&#10;Z7Jetu2wEnBMrpf3f5KN9HnIT6/Wx2JOTfskz0/sVuxzGz9478iSnpqXCX4Xpj3YBNikycx6FzWx&#10;2+jVOuYrvG7fUa1uW9gbg+XwAA0u64XY8KYseQVpAmFdtWJ3XwamWbmflcAX8g5GKMF79m4V9kne&#10;Ip9dLN44I2oXFzXvh78GX6okvWxR/zgKqNNMtQ==&#10;        </string>
    </set>
    <string name="long_key">dJZBbPEUP1f1nlFlu1K7/Vz20djyPFtNSU+/hTd3pPn0B8YlOdRUCSM7U7RXRsxoXHZL3+oaYiId&#10;1tBlgm18EqIXWraX9Ro0Mo6RiWKr/eil8YNJn8/LtdhLvmgJMyFL2AnxXk7G7/xP/T0FDtmNpOiv&#10;AydL07FZqPQ9O3vfHxlpphKEGq0js9GJsWdxwBt+2JaIiRX8S2RZLb6FT3ANAXYjpl/WU13fFJ3V&#10;656nGNaJaNDDw38wixIavm90lYpGUWpgs89nqRIHhfUjx6N+kOOT9fUjdgHEU7Uj9PKaSkxpJa5A&#10;nkm9f4vHZxGocBPfKriODw5V43S9zSboveqSbg==&#10;    </string>
    <string name="int_key">SFWNQWmHnZKFm4OV75osSWegpE5DsY93Mc+zomME2bIhB+QBgcgLaH0lO7Xa6DXsUQakbNIlO4s0&#10;QeY5ll8usPJRKxYqUnuseQJJA1PSjKFljQy7ou+GDeDjDuskWfdK2rm+9GTadROTbgOFfjDch/1J&#10;D1zSBZhyOk3eqWdq3caP8RWSGz4ez4ei/rEB/q5VMAj+dO1zAzGjzzaslYadCXgRfhLLz49Qbyc8&#10;WxZgSZez2kAv4/nZ/AqDWTmwv+7tXZORub/hDiDWK9fDBqG9cTeWuRV1H4zl7k3ToWZPGbdDygxq&#10;Q/icTh94tJLeR95+rXMRVwYXXHGzy+NZd8nqdQ==&#10;    </string>
    <string name="boolean_key">LoGdVG6PPOBR/7vjdiFYBjuS17VRLOipJIUZrFY6lRlrvSfnP1veTyOGgxr0I8sY2H96HU71oDqX&#10;HMswLI8VRz5aMavCZqnFHg3WuSx85NfkZdQfEY8Jft4lnUwplDM1xlpXIr6ROWF+n1+rUwEh1e+I&#10;R9TYkiQ+tgDMcqgJzxq+JVQ6JuJLS1ANTHpBb5Ig1IHZA+zHxKYDmlmWTiVBw57pfUBY7gqZUK7R&#10;9I8ynJXmaLpLxtskVWp5RmUzNdstU+gyDTN89c7TjiTYuINHZbR11Quj7bqAysVQNvxVQyUh8bXt&#10;MSl0iETYo2eZcIr4zfTIUj3mpOa7lBvz+B8Hww==&#10;    </string>
</map>
```